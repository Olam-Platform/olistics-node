package com.olam.node.service.infrastructure.storage;

public interface IDataStorageService {
    String getDataIdentifier(byte[] data);

    String save(byte[] data);

    byte[] loadData(String url);
}
