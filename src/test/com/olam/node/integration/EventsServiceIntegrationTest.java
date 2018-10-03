//package com.olam.node.integration;
//
//import com.olam.node.service.application.EventsService;
//import com.olam.node.service.application.entities.EventData;
//import com.olam.node.service.application.entities.EventType;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Observable;
//
//public class EventsServiceIntegrationTest {
//
//    @Autowired
//    EventsService service;
//
//    EventsServiceObservable observable = new EventsServiceObservable(3);
//
//    @Before
//    public void setUp() throws Exception {
//        //redis server needs to run - localhost:6349
//
//    }
//
//    @Test
//    public void subscribe() {
//
//    }
//
//    @Test
//    public void getSubscription() {
//    }
//
//    @Test
//    public void deleteSubscription() {
//    }
//
//    private class EventsServiceObservable extends Observable {
//        private int n = 0;
//
//        public EventsServiceObservable(int n) {
//            this.n = n;
//        }
//
//        public void setValue(int n) {
//            this.n = n;
//            setChanged();
//            notifyObservers(new EventData("0x234562", "0x12345",EventType.SHIPMENT_CREATED,"1234"));
//        }
//
//        public int getValue() {
//            return n;
//        }
//
//
//    }
//}