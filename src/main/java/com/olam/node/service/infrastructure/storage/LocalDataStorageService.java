package com.olam.node.service.infrastructure.storage;

public class LocalDataStorageService implements IDataStorageService {
    @Override
    public String getDataIdentifier(byte[] data) {
        return null;
    }

    @Override
    public String save(byte[] data) {
        return null;
    }

    @Override
    public byte[] loadData(String url) {
        return new byte[0];
    }
}
