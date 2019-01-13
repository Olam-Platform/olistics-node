package com.olam.node.service.application.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class SubscribeData {
    private String signature;
    private String shipmentId;
    private String callbackUrl;
    private EventType event;

    @JsonCreator
    public SubscribeData(@JsonProperty("signature") String signature,
                         @JsonProperty("shipmentId")String shipmentId,
                         @JsonProperty("callbackUrl")String callbackUrl,
                         @JsonProperty("event") EventType event) {
        this.signature = signature;
        this.shipmentId = shipmentId;
        this.callbackUrl = callbackUrl;
        this.event = event;
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

    public EventType getEvent() {
        return event;
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    /*
    @Override
    public String toString() {
        return "SubscribeData{" +
                "signature='" + signature + '\'' +
                ", shipmentId='" + shipmentId + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", event=" + event +
                '}';
    }
    */
}
