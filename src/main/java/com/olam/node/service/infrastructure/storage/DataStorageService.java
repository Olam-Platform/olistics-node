package com.olam.node.service.infrastructure.storage;

import org.springframework.core.io.Resource;

public interface DataStorageService {

    String getdataIdentifier(byte[] data);

    String save(byte[] data);

    Resource loadDataAsResource(String hash);
}
