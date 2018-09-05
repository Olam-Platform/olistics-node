package com.olam.node.integration;

import com.olam.node.service.infrastructure.storage.IPFSDataStorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@EnableConfigurationProperties
@RunWith(SpringRunner.class)
public class IPFSDataStorageServiceTest {

    @Autowired
    private IPFSDataStorageService ipfsService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getdataIdentifier() {
        String test = "tomer";
        ipfsService.getdataIdentifier(test.getBytes());
    }

    @Test
    public void save() {
    }

    @Test
    public void loadDataAsResource() {
    }
}