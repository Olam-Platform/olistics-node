package com.olam.node.configuration;

import io.ipfs.api.IPFS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IPFSConfig {

    @Value("${ipfs.node.address}")
    private String ipfsDaemonAdress;

    @Bean
    public IPFS getIPFSDaemon(){
        return new IPFS(ipfsDaemonAdress);
    }


}
