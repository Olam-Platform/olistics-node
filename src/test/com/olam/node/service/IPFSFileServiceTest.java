package com.olam.node.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = IPFSFileService.class)
public class IPFSFileServiceTest {


    IPFSFileStorageService service;

    @Before
    public void setUp() throws Exception {
//        service = new IPFSFileStorageService("/ip4/127.0.0.1/tcp/5001");

    }

    @Test
    public void save() throws IOException {
//        File cat = ResourceUtils.getFile("classpath:cat.jpg");
//        String hash = service.save(cat);
//        Resource file = service.loadFileAsResource(hash);
////        file.getFile().equals(cat);


    }
}