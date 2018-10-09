package com.olam.node.configuration;

import com.olam.node.service.infrastructure.blockchain.EthereumNodeService;
import com.olam.node.service.infrastructure.blockchain.EthereumNodeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EthereumNodeConfig {

    public static final Logger LOG = LoggerFactory.getLogger(EthereumNodeConfig.class);

    @Value("${websocket.url.infura}")
    private String rpcUrl;

    @Bean
    public EthereumNodeService getEthereumNodeService() {
        LOG.debug("connecting to Ethereum node at: " + rpcUrl);
        return new EthereumNodeServiceImpl(rpcUrl);
    }
}
