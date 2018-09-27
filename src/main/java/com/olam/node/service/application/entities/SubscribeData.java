package com.olam.node.service.application.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class SubscribeData {



    private String signature;
    private String shipmentId;
    private String callbackUrl;
    private List<EventType> events;

    @JsonCreator
    public SubscribeData(@JsonProperty("signature") String signature,
                         @JsonProperty("shipmentId")String shipmentId,
                         @JsonProperty("callbackUrl")String callbackUrl,
                         @JsonProperty("events") List<EventType> events) {
        this.signature = signature;
        this.shipmentId = shipmentId;
        this.callbackUrl = callbackUrl;
        this.events = events;
    }

    public SubscribeData() {
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public List<EventType> getEvents() {
        return events;
    }

    public void setEvents(List<EventType> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "SubscribeData{" +
                "signature='" + signature + '\'' +
                ", shipmentId='" + shipmentId + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", events=" + events +
                '}';
    }
}
