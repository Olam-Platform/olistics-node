package com.olam.node.service.application;

import org.web3j.crypto.Credentials;

import java.util.Dictionary;
import java.util.Hashtable;

public interface ICredentialsService {
    Dictionary<String,Credentials> credentialsRepository = new Hashtable<>();
    Credentials getCredentials(String address, String password);
}
