package com.olam.node.service.infrastructure.blockchain;

import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigInteger;

public class TransportObserverImpl implements TransportObserver {
    private String contractAddress = null;
    private String fromAddress;
    private String toAddress;
    private BigInteger contractBirthdayBlock = BigInteger.ZERO;
    boolean eventDetected;
    Transaction transaction;

    public TransportObserverImpl(String notifyAddress) {
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
    public String getContractAddress() { return contractAddress; }

    @Override
    public void setContractAddress(String contractAddress) {this.contractAddress = contractAddress; }

    @Override
    public BigInteger getBirthdayBlock() { return contractBirthdayBlock; }

    @Override
    public void setBirthdayBlock(BigInteger birthdayBlock) { this.contractBirthdayBlock = birthdayBlock; }

    @Override
    public boolean getEventDetected() {return eventDetected; }

    @Override
    public void setEventDetected(boolean eventDetected) { this.eventDetected = eventDetected; }

    @Override
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }

    @Override
    public Transaction getTransaction() { return transaction; }
}
