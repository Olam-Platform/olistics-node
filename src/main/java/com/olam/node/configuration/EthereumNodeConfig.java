package com.olam.node.configuration;

import com.olam.node.service.infrastructure.EthereumNodeService;
import com.olam.node.service.infrastructure.EthereumNodeServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EthereumNodeConfig {

    @Value("${rpcurl.ethereum.rinkeby}")
    private String rpcUrl;

    @Bean
    public EthereumNodeService getEthereumNodeService() {

        return new EthereumNodeServiceImpl(rpcUrl);
    }
}
