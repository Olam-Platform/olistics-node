package com.olam.node.service.infrastructure.storage;

public interface DataStorageService {

    String getDataIdentifier(byte[] data);

    String save(byte[] data);

    byte[] loadData(String hash);
}
