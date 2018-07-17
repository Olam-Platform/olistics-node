package com.olam.node.sdk;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@RunWith(SpringRunner.class)
public class IPFSClusterTestIntegration {


    private IPFSCluster cluster;
    private File file;

    @Before
    public void setUp() throws Exception {
        this.file = ResourceUtils.getFile("classpath:resources/cat.jpg");
        this.cluster = new IPFSCluster("localhost", 9094, "http");
    }

    @Test
    public void checkIpfsClusterNodeId() {
        ResponseEntity<String> ls = cluster.id();
        String body = ls.getBody();
        System.out.println(body);
    }

    @Test
    public void testStatusWhenCidExists() {
        ResponseEntity<String> ls = cluster.pins.status("123");
        String body = ls.getBody();
        System.out.println(body);
    }

    @Test
    public void testSSyncWithCid() {
        ResponseEntity<String> ls = cluster.pins.sync();
        String body = ls.getBody();
        System.out.println(body);
    }

}