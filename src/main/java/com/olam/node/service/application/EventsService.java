package com.olam.node.service.application;
/*
import com.olam.node.service.application.entities.EventData;
import com.olam.node.service.application.entities.EventType;
import com.olam.node.service.infrastructure.blockchain.IEthereumNodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
    private Jedis redisClient;

    @Autowired
    private IEthereumNodeService ethereumNode;

    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        LOG.debug("initializing Events Service");
        restTemplate = new RestTemplate();
    }

    public Long subscribe(String address, EventType event, String url) {
        Long result = redisClient.hset(address, event.toString(), url);

        if (event.equals(EventType.SHIPMENT_CREATED)) {
            ethereumNode.registerForShipmentEvent(new EventObserver());
        } else {
            ethereumNode.registerForDocumentEvent(new EventObserver());
        }

        return result;
    }

    public String getSubscription(String address, EventType event) {
        return redisClient.hget(address, event.toString());
    }

    public Long deleteSubscription(String address, EventType event) {
        return redisClient.hdel(address, event.toString());
    }

    private class EventObserver implements Observer {
        private ResponseEntity<String> postToCallback(EventData data, String url) {
            //TODO: add more robust headers
            HttpHeaders requestHeaders = new HttpHeaders();
            HttpEntity<EventData> requestEntity = new HttpEntity<>(data, requestHeaders);
            return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        }

        @Override
        public void update(Observable o, Object arg) {
            if (arg instanceof EventData) {
                EventData data = (EventData) arg;
                String url = getSubscription(data.getAddress(), data.getEvent());
                if (url != null) {
                    this.postToCallback(data, url);
                }
            }
        }
    }
}
*/