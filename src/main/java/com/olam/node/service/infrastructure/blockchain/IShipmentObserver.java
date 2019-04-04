package com.olam.node.service.infrastructure.blockchain;

import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigInteger;

public interface IShipmentObserver {
    String getFrom();
    void setFrom(String fromAddress);

    String getTo();
    void setTo(String toAddress);

    boolean getEventDetected();
    void setEventDetected(boolean transportCreated);

    void setTransaction(Transaction transaction);

    Transaction getTransaction();
}
