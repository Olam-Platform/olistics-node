package com.olam.node.service.application.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShipmentEvent {
    private String      shipmentId;
    private String      triggeredBy;
    private EventType   eventType;
    private String      eventData;

    @JsonCreator
    public ShipmentEvent(/*String shipmentId, String triggeredBy, EventType eventType, */@JsonProperty("event") String eventData) {
        this.eventData = eventData;
        //this.eventType = eventType;
        //this.triggeredBy = triggeredBy;
        //this.shipmentId = shipmentId;
    }

    @JsonGetter
    public String ShipmentId() {return shipmentId; }
    @JsonGetter
    public String TriggeredBy() {return triggeredBy; }
    @JsonGetter
    public EventType EventType() {return eventType; }
    @JsonGetter("event")
    public String EventData() {return eventData; }
}
