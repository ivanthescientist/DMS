package com.isc.dms;

import com.isc.dms.storage.StorageEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class StorageController {

    @Autowired
    private StorageServer storageServer;

    @RequestMapping(value = "/value/{key}", method = RequestMethod.GET)
    public String getValue(@PathVariable String key) {
        return storageServer.get(key);
    }

    @RequestMapping(value = "/value/{key}", method = RequestMethod.PUT)
    public String setValue(@PathVariable String key, @RequestBody String value) {
        storageServer.set(key, value);

        return storageServer.get(key);
    }
}
