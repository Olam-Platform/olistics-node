package com.olam.node.service.infrastructure.blockchain;

import java.math.BigInteger;

public interface TransportObserver {
    String getFrom();
    void setFrom(String fromAddress);

    String getTo();
    void setTo(String toAddress);

    String getContractAddress();
    void setContractAddress(String contractAddress);

    BigInteger getBirthdayBlock();
    void setBirthdayBlock(BigInteger birthdayBlock);
}
