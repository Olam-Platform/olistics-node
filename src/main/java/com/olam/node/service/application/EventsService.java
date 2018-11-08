package com.olam.node.service.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olam.node.service.application.entities.EventData;
import com.olam.node.service.application.entities.EventType;
import com.olam.node.service.infrastructure.blockchain.EthereumNodeService;
import com.olam.node.service.infrastructure.blockchain.TransportObserverImpl;
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
import rx.Subscription;

import javax.annotation.PostConstruct;
import java.util.Observable;
import java.util.Observer;

@Service
public class EventsService {

    private static final Logger LOG = LoggerFactory.getLogger(EventsService.class);

    @Autowired
    private Jedis jedis;

    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private EthereumNodeService ethereumNode;

    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        LOG.debug("initializing Events Service");
        restTemplate = new RestTemplate();
    }


    public Long subscribe(String address, EventType event, String url) {
        Long result = jedis.hset(address, event.toString(), url);
        Subscription subscription = null;
        if (event.equals(EventType.SHIPMENT_CREATED)) {
            subscription = this.subscribeToShipmentCreatedEvent(address, url);
        } else {
            //todo: add subscription to other events
        }
        //todo: save subscription to DB

        return result;
    }

    private Subscription subscribeToShipmentCreatedEvent(String address, String url) {
        TransportObserverImpl transportObserver = new TransportObserverImpl(address);
        Subscription subscription = ethereumNode.registerForTransportCreatedEvent(transportObserver);

        Thread waiter = new Thread(() -> {
            try {
                LOG.info(">>>>>> in thread: wait for transport created event");
                ethereumNode.waitForEvent(transportObserver);
                LOG.info(">>>>>> in thread: caught transport created event");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        waiter.start();
        LOG.info(">>>>>> starting thread");
        return subscription;
    }

    public String getSubscription(String address, EventType event) {
        return jedis.hget(address, event.toString());
    }

    public Long deleteSubscription(String address, EventType event) {
        return jedis.hdel(address, event.toString());
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
