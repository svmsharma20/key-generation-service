
package com.shivam.keygenerationservice.keygen;

import com.shivam.keygenerationservice.model.KeyEntry;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class KeyGeneratorImpl implements KeyGenerator {

    @Override
    public Set<KeyEntry> getUniqueKeys(long count) {
        Set<KeyEntry> keyEntrySet = new HashSet<>();

        while (keyEntrySet.size()< count){
            ForkJoinTask<Set<KeyEntry>> keyGenerationTask = new KeyGenerationTask(count, count);
            ForkJoinPool pool = new ForkJoinPool(5);
            Set<KeyEntry> set = pool.invoke(keyGenerationTask);
            keyEntrySet.addAll(set);
        }

        return keyEntrySet;
    }
}
