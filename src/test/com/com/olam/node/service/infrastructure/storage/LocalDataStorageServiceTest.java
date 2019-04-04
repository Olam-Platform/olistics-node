package com.olam.node.service.infrastructure.storage;

import com.olam.node.service.application.entities.StorageProtocol;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocalDataStorageServiceTest {


    @Test
    public void testGetStorageProtocol () {
        StorageProtocol storageProtocol = IStorageService.getStorageProtocol("D:\\");
        assertEquals(storageProtocol, storageProtocol.LOCAL);

        storageProtocol = IStorageService.getStorageProtocol("d:\\");
        assertEquals(storageProtocol, storageProtocol.LOCAL);

        storageProtocol = IStorageService.getStorageProtocol("DD:\\");
        assertNotEquals(storageProtocol, storageProtocol.LOCAL);
    }
}