package com.olam.node.sdk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.geom.Line2D;

public class IPFSCluster {

    RestTemplate restClient;

    private final String host;
    private final int port;
    public final Pins pins = new Pins();

    public IPFSCluster(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public class Pins {

        public String ls() {
            return "hello";
        }


    }

}
