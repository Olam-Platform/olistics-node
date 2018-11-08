package com.olam.node.web;

import com.olam.node.service.application.EventsService;
import com.olam.node.service.application.entities.EventType;
import com.olam.node.service.application.entities.SubscribeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/subscriptions")
public class EventsController {

    private static final Logger logger = LoggerFactory.getLogger(EventsController.class);

    @Autowired
    private EventsService eventsService;

    @GetMapping
    public ResponseEntity<String> getSubscription(@RequestParam String address, @RequestParam EventType event) {
        logger.debug("inside get subscription method");
        //TODO: throw exception if url is not available?
        String callback = eventsService.getSubscription(address, event);
        logger.debug(String.format("sending to user: %s, callback url: %s", address, callback));
        return new ResponseEntity(callback, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createSubscription(@RequestBody SubscribeData data) {
        eventsService.subscribe(data.getSignature(), data.getEvent(), data.getCallbackUrl());
        logger.debug(String.format("user subscribed to event %s in shipment %s with url %s", data.getEvent().toString(), data.getShipmentId(), data.getCallbackUrl()));
        ResponseEntity entity = new ResponseEntity(String.format("subscription to [shipment: %s , event: %s, callback: %s] created"
                , data.getShipmentId(), data.getEvent(), data.getCallbackUrl()), HttpStatus.CREATED);
        return entity;
    }

    @PutMapping
    public ResponseEntity<String> updateSubscription(@RequestBody SubscribeData data) {
        eventsService.subscribe(data.getSignature(), data.getEvent(), data.getCallbackUrl());
        logger.debug(String.format("user updated subscription to event %s in shipment %s with url %s", data.getEvent().toString(), data.getShipmentId(), data.getCallbackUrl()));
        ResponseEntity entity = new ResponseEntity(String.format("subscription to [shipment: %s , event: %s, callback: %s] updated" +
                " - succeeded!", data.getShipmentId(), data.getEvent(), data.getCallbackUrl()), HttpStatus.CREATED);
        return entity;
    }

    @DeleteMapping
    public ResponseEntity<String> deleteSubscription(@RequestParam String address, @RequestParam EventType event) {
        eventsService.deleteSubscription(address, event);
        logger.debug(String.format("user %s deleted subscription to event %s", address, event));
        return new ResponseEntity(String.format("deleted subscription to event %s", event), HttpStatus.ACCEPTED);
    }
}
