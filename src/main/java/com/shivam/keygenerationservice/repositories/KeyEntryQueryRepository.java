
package com.shivam.keygenerationservice.repositories;

import com.shivam.keygenerationservice.model.KeyEntry;

import java.util.Set;

public interface KeyEntryQueryRepository {
    String getKey();
    long addKeysToDB(Set<KeyEntry> keyEntrySet);
    long getAvailableKeyCount();
}
