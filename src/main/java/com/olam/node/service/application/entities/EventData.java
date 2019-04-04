package com.olam.node.service.application.entities;

public class EventData {
    private String shipmentId;
    private String address;
    private EventType event;
    private String documentId;

    public EventData(String shipmentId, String address, EventType event, String documentId) {
        this.shipmentId = shipmentId;
        this.address = address;
        this.event = event;
        this.documentId = documentId;
    }

    public EventData(String shipmentId, String address, EventType event) {
        this.shipmentId = shipmentId;
        this.address = address;
        this.event = event;
        this.documentId = null;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public EventType getEvent() {
        return event;
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
