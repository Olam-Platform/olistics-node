package com.olam.node.service.infrastructure.blockchain;

import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigInteger;

public interface ITransportObserver {
    String getFrom();
    void setFrom(String fromAddress);

    String getTo();
    void setTo(String toAddress);

    String getContractAddress();
    void setContractAddress(String contractAddress);

    BigInteger getBirthdayBlock();
    void setBirthdayBlock(BigInteger birthdayBlock);

    boolean getEventDetected();
    void setEventDetected(boolean transportCreated);

    void setTransaction(Transaction transaction);

    Transaction getTransaction();
}
