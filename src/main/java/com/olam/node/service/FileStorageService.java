package com.olam.node.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String save(MultipartFile file);

    Resource loadFileAsResource(String hash);

}
