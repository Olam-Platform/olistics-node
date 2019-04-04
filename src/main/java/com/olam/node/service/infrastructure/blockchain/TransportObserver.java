package com.olam.node.service.infrastructure.blockchain;

import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigInteger;

public class TransportObserver implements IShipmentObserver {
    private String  fromAddress;
    private String  toAddress;
    Transaction     transaction;

    boolean eventDetected;

    public TransportObserver(String notifyAddress) {
        toAddress = notifyAddress;
    }

    @Override
    public String getFrom() { return fromAddress; }

    @Override
    public void setFrom(String fromAddress) { this.fromAddress = fromAddress; }

    @Override
    public String getTo() { return toAddress; }

    @Override
    public void setTo(String toAddress) { this.toAddress = toAddress; }

    @Override
    public boolean getEventDetected() {return eventDetected; }

    @Override
    public void setEventDetected(boolean eventDetected) { this.eventDetected = eventDetected; }

    @Override
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }

    @Override
    public Transaction getTransaction() { return transaction; }
}
