package com.olam.node.service.infrastructure.storage;

import com.olam.node.service.application.entities.Collaborator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.IOException;


@Service
public class DirectoryService implements IDirectoryService {
    private static final Logger LOG = LoggerFactory.getLogger(DirectoryService.class);
    private static String FIELD_NAME = "name";
    private static String FIELD_ADDRESS = "address";
    private static String FIELD_KEYSTOREFILEPATH = "keystorefile";
    private static String FIELD_CALLBACKURL = "callbackurl";

    private Jedis redisClient;

    public DirectoryService() {
        //loadTestCredentials();
        redisClient = new Jedis("127.0.0.1");
    }

    @Override
    public boolean addCollaborator(Collaborator collaborator, String keyStoreFilePath) {
        redisClient.hset(collaborator.Address().toLowerCase(), FIELD_NAME, collaborator.Name());
        redisClient.hset(collaborator.Address().toLowerCase(), FIELD_ADDRESS, collaborator.Address());    // redundant. remove
        redisClient.hset(collaborator.Address().toLowerCase(), FIELD_KEYSTOREFILEPATH, keyStoreFilePath);
        redisClient.hset(collaborator.Address().toLowerCase(), FIELD_CALLBACKURL, collaborator.CallbackUrl());

        return false;
    }

    @Override
    public boolean collaboratorExists(String address) {
        return redisClient.exists(address.toLowerCase());
    }

    @Override
    public String getCollaboratorName(String address) {
        return redisClient.hget(address.toLowerCase(), "name");
    }

    @Override
    public String getCollaboratorKeyStoreFilePath(String address) {
        return redisClient.hget(address.toLowerCase(), FIELD_KEYSTOREFILEPATH);
    }

    @Override
    public String getCallbackUrl(String address) {
        return redisClient.hget(address.toLowerCase(), FIELD_CALLBACKURL);
    }
}
