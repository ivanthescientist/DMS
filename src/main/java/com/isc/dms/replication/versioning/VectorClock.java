package com.isc.dms.replication.versioning;


import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class VectorClock {
    private Map<String, Integer> versions;

    public VectorClock() {
        versions = new HashMap<>();
    }

    public VectorClock(Collection<String> nodeIdList) {
        versions = new HashMap<>(nodeIdList.size());
        nodeIdList.forEach(nodeId -> versions.put(nodeId, 0));
    }

    public void increment(String nodeId) {
        versions.compute(nodeId, (key, version) -> version == null ? 1 : version + 1);
    }

    public VectorClock merge(VectorClock other) {
        VectorClock newClock = new VectorClock(Sets.union(this.versions.keySet(), other.versions.keySet()));

        for(String key : newClock.versions.keySet()) {
            int keyVersion = Integer.max(other.versions.getOrDefault(key, 0), this.versions.getOrDefault(key, 0));
            newClock.versions.put(key, keyVersion);
        }

        return newClock;
    }

    public Map<String, Integer> getVersions() {
        return versions;
    }

    public void setVersions(Map<String, Integer> versions) {
        this.versions = versions;
    }
}
