package com.olam.node.service.application;

import com.olam.node.service.application.entities.EventType;
import com.olam.node.service.infrastructure.blockchain.EthereumNodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.Observable;
import java.util.Observer;

@Service
public class EventsService {

    private static final Logger LOG = LoggerFactory.getLogger(EventsService.class);

    @Autowired
    private Jedis jedis;

    @Autowired
    private EthereumNodeService ethereumNode;

    private RestTemplate client;

    @PostConstruct
    public void init() {
        LOG.debug("initializing Events Service");
        client = new RestTemplate();
    }


    public Long subscribe(String address, EventType event, String url) {
        Long result = jedis.hset(address, event.toString(), url);


        if (event.equals(EventType.SHIPMENT_CREATED)) {
//            ethereumNode.registerForShipmentEvent();
        } else {
//            ethereumNode.registerForDocumentEvent();

        }
        return result;
    }

    public String getSubscription(String address, EventType event) {
        return jedis.hget(address, event.toString());
    }

    public Long deleteSubscription(String address, EventType event) {
        return jedis.hdel(address, event.toString());
    }

//    private class EventObserver implements Observer{
//
//        @Override
//        public void update(Observable o, Object arg) {
//
//        }
//    }

}
