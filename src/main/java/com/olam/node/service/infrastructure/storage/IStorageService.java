package com.olam.node.service.infrastructure.storage;

import com.olam.node.service.application.entities.StorageProtocol;
import io.ipfs.api.IPFS;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public interface IStorageService {
    String  getDataIdentifier(byte[] data);

    String  save(byte[] data);

    void    saveTo(byte[] data, String url) throws Exception;

    byte[]  read(String url) throws Exception;

    static  StorageProtocol getStorageProtocol(String url) {
        StorageProtocol storageProtocol = StorageProtocol.LOCAL;

        if (url.toLowerCase().startsWith("ftp")) {
            storageProtocol = StorageProtocol.FTP;
        }
        else if (url.toLowerCase().startsWith("http")) {
            storageProtocol = StorageProtocol.HTTP;
        }
        else if (url.toLowerCase().startsWith("ipfs")) {
            storageProtocol = storageProtocol.IPFS;
        }

        return storageProtocol;
    }

    static IStorageService getDataStorageService(String url) {
        switch (getStorageProtocol(url)) {
            case LOCAL: return new LocalDataStorageService();
            case IPFS:  return new IPFSService(new IPFS(url));
            case FTP:   return new FTPStorageService();
            default: return null;
        }
    }
}
