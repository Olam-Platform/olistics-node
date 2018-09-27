package com.olam.node.configuration;

import io.ipfs.api.IPFS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class JedisConfig {

    @Value("${redis.host}")
    private String host;

    @Bean
    public Jedis getRedisClient() {
        return new Jedis(host);
    }

}
