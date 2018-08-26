package com.olam.node.service.infrastructure;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface DataStorageService {

    String getdataIdentifier(byte[] data);

    String save(byte[] data);

    Resource loadDataAsResource(String hash);
}
