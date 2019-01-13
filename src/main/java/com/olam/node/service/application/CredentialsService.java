package com.olam.node.service.application;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

@Service
public class CredentialsService implements ICredentialsService {
    public CredentialsService() {
        loadTestCredentials();
    }

    @Override
    public Credentials getCredentials(String address, String password) {
        return credentialsRepository.get(address.toLowerCase());
    }

    private static void loadTestCredentials() {
        try {
            //rinkeby
            ArrayList<Credentials> credentials = new ArrayList<>();

            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file01.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file02.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file03.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file04.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file05.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file06.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file07.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file08.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file09.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file10.json")));

            // ganache
            Properties properties = new Properties();
            properties.load(new FileReader(ResourceUtils.getFile("classpath:application.properties")));

            credentials.add(Credentials.create(properties.getProperty("ganache.privatekey01")));
            credentials.add(Credentials.create(properties.getProperty("ganache.privatekey02")));
            credentials.add(Credentials.create(properties.getProperty("ganache.privatekey03")));
            credentials.add(Credentials.create(properties.getProperty("ganache.privatekey04")));
            credentials.add(Credentials.create(properties.getProperty("ganache.privatekey05")));

            for (Credentials credential: credentials) {
                credentialsRepository.put(credential.getAddress().toLowerCase(), credential);
            }
        } catch (CipherException | IOException e) {
            e.printStackTrace();
        }
    }
}
