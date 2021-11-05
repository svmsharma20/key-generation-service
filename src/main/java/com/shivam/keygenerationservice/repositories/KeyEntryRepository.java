
package com.shivam.keygenerationservice.repositories;

import com.shivam.keygenerationservice.model.KeyEntry;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface KeyEntryRepository extends CassandraRepository<KeyEntry, String> {

}
