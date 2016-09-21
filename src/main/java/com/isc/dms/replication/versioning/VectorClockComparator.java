package com.isc.dms.replication.versioning;


import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VectorClockComparator {
    public Occurrence compare(VectorClock left, VectorClock right) {
        int leftGreater = 0;
        int rightGreater = 0;

        Map<String, Integer> leftVersions = left.getVersions();
        Map<String, Integer> rightVersions = right.getVersions();
        Sets.SetView<String> keys = Sets.union(leftVersions.keySet(), rightVersions.keySet());

        for(String key : keys) {
            int leftValue = leftVersions.getOrDefault(key, 0);
            int rightValue = rightVersions.getOrDefault(key, 0);

            if(leftValue > rightValue) {
                leftGreater++;
            } else if(leftValue < rightValue) {
                rightGreater++;
            }
        }

        if(leftGreater > rightGreater) {
            return Occurrence.BEFORE;
        } else if(leftGreater < rightGreater) {
            return Occurrence.AFTER;
        } else {
            return Occurrence.CONCURRENT;
        }
    }
}
