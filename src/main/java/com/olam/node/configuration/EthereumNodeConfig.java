package com.olam.node.configuration;

import com.olam.node.service.infrastructure.blockchain.EthereumNodeServiceImpl;
import com.olam.node.service.infrastructure.blockchain.EthereumNodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EthereumNodeConfig {

    @Value("${websocket.url.infura}")
    private String rpcUrl;

    @Bean
    public EthereumNodeService getEthereumNodeService() {

        return new EthereumNodeServiceImpl(rpcUrl);
    }
}
