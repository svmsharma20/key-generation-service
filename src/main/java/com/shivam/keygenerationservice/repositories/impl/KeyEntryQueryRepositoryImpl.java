
package com.shivam.keygenerationservice.repositories.impl;

import com.datastax.oss.driver.api.core.CqlSession;
import com.shivam.keygenerationservice.exception.KeyNotFoundException;
import com.shivam.keygenerationservice.keygen.KeyGenerator;
import com.shivam.keygenerationservice.model.KeyEntry;
import com.shivam.keygenerationservice.repositories.KeyEntryQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraBatchOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.cassandra.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Repository
public class KeyEntryQueryRepositoryImpl implements KeyEntryQueryRepository {
    @Autowired
    private KeyGenerator keyGenerator;
    private static long SLA = 50000;
    private String tableName = "keytable";
    private int retryAttempts = 3;
    private CqlSession cqlSession;
    private CassandraTemplate template;
    private boolean isInitialized;

    @Override
    public String getKey() {
        if (!isInitialized) {
            init();
        }
        boolean result = false;
        String key = null;
        int retry = 0;
        do {
            synchronized (this) {
                KeyEntry keyEntry = template.selectOne(Query.query(Criteria.where("isavailable").is(true)), KeyEntry.class);
                if (keyEntry == null) {
                    update();
                    throw new KeyNotFoundException();
                }
                key = keyEntry.getKey();
                result = updateKeyEntry(key, template);
            }
            retry++;
        } while (!result && retry < retryAttempts);
        update();
        if (!result) {
            throw new KeyNotFoundException();
        }
        return key;
    }

    private void init() {
        cqlSession = CqlSession.builder().withKeyspace("keystore").build();
        ;
        template = new CassandraTemplate(cqlSession);
        isInitialized = true;
    }

    @Override
    public long getAvailableKeyCount() {
        long count = template.count(Query.query(Criteria.where("isavailable").is(true)), KeyEntry.class);
        return count;
    }

    @Override
    public long addKeysToDB(Set<KeyEntry> keyEntrySet) {
        CassandraBatchOperations cassandraBatchOperations = new CassandraTemplate(cqlSession).batchOps();
        return cassandraBatchOperations.insert(keyEntrySet).execute().getRows().size();
    }

    synchronized private boolean updateKeyEntry(String key, CassandraTemplate template) {
        Update update = Update.update("isavailable", false);
        return template.update(Query.query(Criteria.where("key").is(key)), update, KeyEntry.class);
    }

    private void update() {
        ExecutorService executorService = null;
        long currentCount = getAvailableKeyCount();
        try {
            if (currentCount / 2 < SLA) {
                long batchSize = 1000;
                long countReq = 2 * SLA - currentCount;
                ArrayList<Future<Long>> futureList = new ArrayList<>();

                while (countReq > 0) {
                    executorService = Executors.newFixedThreadPool(5);
                    Future<Long> future = executorService.submit(new KeyEntryAdderTask(batchSize));
                    futureList.add(future);
                    countReq -= batchSize;
                    if(countReq<batchSize){
                        batchSize = countReq;
                    }
                }
            }
        } finally {
            if(executorService!=null){
                executorService.shutdownNow();
            }
        }
    }

    class KeyEntryAdderTask implements Callable<Long> {
        private long batchSize;

        public KeyEntryAdderTask(long batchSize) {
            this.batchSize = batchSize;
        }

        @Override
        public Long call() throws Exception {
            Set<KeyEntry> keyEntrySet = keyGenerator.getUniqueKeys(batchSize);
            Long rowCount = addKeysToDB(keyEntrySet);
            return rowCount;
        }
    }
}
