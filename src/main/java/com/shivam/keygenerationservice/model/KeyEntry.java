
package com.shivam.keygenerationservice.model;

import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Table("keytable")
public class KeyEntry implements Serializable {

    @PrimaryKey
    private String key;

    @Indexed
    private boolean isAvailable;

    public KeyEntry() {
    }

    public KeyEntry(String key, boolean isAvailable) {
        this.key = key;
        this.isAvailable = isAvailable;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof KeyEntry && this.key.equals(((KeyEntry)((KeyEntry) obj)).key)){
            return true;
        }
        return false;
    }
}
