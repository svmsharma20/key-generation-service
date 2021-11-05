package com.shivam.keygenerationservice.keygen;

import com.shivam.keygenerationservice.model.KeyEntry;

import java.util.Set;

public interface KeyGenerator {

    Set<KeyEntry> getUniqueKeys(long size);
}
