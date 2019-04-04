package com.olam.node.service.infrastructure.storage;

import com.olam.node.service.application.entities.StorageProtocol;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ws.rs.NotSupportedException;
import java.io.File;
import java.io.IOException;

@Service
public class LocalDataStorageService implements IStorageService {
    @Override
    public String getDataIdentifier(byte[] data) {
        throw new NotImplementedException();
    }

    @Override
    public void saveTo(byte[] data, String path) {
        assert (IStorageService.getStorageProtocol(path).equals(StorageProtocol.LOCAL));

        File fileToSave = new File(path);

        try {
            FileUtils.writeByteArrayToFile(fileToSave, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String save(byte[] data) {
        throw new NotSupportedException();
    }

    public byte[] read(String path) {
        assert (IStorageService.getStorageProtocol(path).equals(StorageProtocol.LOCAL));
        File fileToRead = new File(path);


        byte[] fileContent = new byte[0];
        try {
            fileContent = FileUtils.readFileToByteArray(fileToRead);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContent;
    }
}
