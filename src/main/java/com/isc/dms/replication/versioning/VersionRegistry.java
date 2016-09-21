package com.isc.dms.replication.versioning;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VersionRegistry {
    private Map<String, VectorClock> keyVersions = new HashMap<>();

    public void update(String key, VectorClock clock) {
        keyVersions.put(key, clock);
    }

    public VectorClock getVersion(String key) {
        return keyVersions.getOrDefault(key, new VectorClock());
    }

    public void increment(String identity, String key) {
        if(!keyVersions.containsKey(key)) {
            keyVersions.put(key, new VectorClock());
        }
        keyVersions.get(key).increment(identity);
    }
}
