package com.olam.node.configuration;

import com.olam.node.sdk.IPFSCluster;
import io.ipfs.api.IPFS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IPFSConfig {

    @Value("${ipfs.node.host}")
    private String ipfsNodeHost;

    @Value("${ipfs.node.port}")
    private int ipfsNodePort;

    @Value("${ipfs.node.multiAddress}")
    private String ipfsNodeMultiAddr;

    @Value("${ipfs.cluster.host}")
    private String ipfsClusterHost;

    @Value("${ipfs.cluster.port}")
    private int ipfsClusterPort;


    @Bean
    public IPFS getIPFSDaemon() {
       return new IPFS(ipfsNodeMultiAddr);
    }


//    @Bean
//    public IPFSCluster getIPFSCluster() {
//        return new IPFSCluster(ipfsClusterHost, ipfsClusterPort);
//    }

}
