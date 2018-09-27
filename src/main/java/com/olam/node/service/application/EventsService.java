package com.olam.node.service.application;

import com.olam.node.service.application.entities.EventType;
import com.olam.node.service.infrastructure.blockchain.EthereumNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class EventsService {

    @Autowired
    Jedis jedis;
    @Autowired
    private EthereumNodeService ethereumNode;

    public String saveCallbackUrl(String address, EventType event, String url) {
        return jedis.set(address + "." + event.toString(), url);
    }

    public String getCallbackUrl(String address, EventType event) {
        return jedis.get(address + "." + event.toString());
    }


}
