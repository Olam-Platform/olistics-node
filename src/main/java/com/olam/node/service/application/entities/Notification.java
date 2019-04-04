package com.olam.node.service.application.entities;

public class Notification {
    private String      shipmentId;
    private String      sourceId;
    private String      targetId;


    public Notification(String shipmentId, String sourceId, String targetId) {
        this.shipmentId = shipmentId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public String ShipmentId()  { return shipmentId; }
    public String SourceId()    { return sourceId; }
    public String TargetId()    { return targetId; }
}
