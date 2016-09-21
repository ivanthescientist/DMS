package com.isc.dms.storage;


import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StorageEngine {
    Map<String, String> storage = new HashMap<>();

    public String set(String key, String value) {
        storage.put(key, value);
        return value;
    }

    public String get(String key) {
        return storage.getOrDefault(key, "");
    }
}
