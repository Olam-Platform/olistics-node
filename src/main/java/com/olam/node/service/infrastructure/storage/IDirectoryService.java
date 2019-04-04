package com.olam.node.service.infrastructure.storage;

import com.olam.node.service.application.entities.Collaborator;
import org.web3j.crypto.Credentials;

import java.util.Dictionary;
import java.util.Hashtable;

public interface IDirectoryService {
    Dictionary<String,Credentials> collaboratorsRepository = new Hashtable<>();

    boolean addCollaborator(Collaborator collaborator, String keyStoreFilePath);

    boolean collaboratorExists(String address);

    String  getCollaboratorName(String address);

    String  getCollaboratorKeyStoreFilePath(String address);

    String  getCallbackUrl(String address);
}
