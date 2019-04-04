package com.olam.node.web;

import com.olam.node.service.application.entities.Collaborator;
import com.olam.node.service.infrastructure.storage.IDirectoryService;
import com.olam.node.service.infrastructure.storage.LocalDataStorageService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

//@Api(description="manage shipment collaborators")
//@RestController
//@RequestMapping("/v1/collaborators")
class CollaboratorsController {
    private static final Logger logger = LoggerFactory.getLogger(CollaboratorsController.class);
    private ClassPathResource classPathResource = new ClassPathResource("application.properties");
    private Properties properties = new Properties();

    @Autowired
    private LocalDataStorageService storageService;
    @Autowired
    private IDirectoryService       directoryService;


    @PostMapping
    public ResponseEntity<String> addCollaborator(@RequestParam String collaboratorId,
                                                  @RequestParam String password,
                                                  @RequestParam String name,
                                                  @RequestParam String keyStoreData,
                                                  @RequestParam String callbackUrl) {
        ResponseEntity<String> response = new ResponseEntity<String>("Failed to add collaborator", HttpStatus.INTERNAL_SERVER_ERROR);

        try {
            Collaborator newCollaborator = new Collaborator(collaboratorId, name);
            newCollaborator.CallbackUrl(callbackUrl);

            properties.load(classPathResource.getInputStream());

            String keystoreFilePath = properties.getProperty("keystore_folder") + collaboratorId.toUpperCase() + ".json";
            storageService.saveTo(keyStoreData.getBytes(), keystoreFilePath);

            directoryService.addCollaborator(newCollaborator, keystoreFilePath);
            response = new ResponseEntity<>("Collaborator added to directory", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping
    public ResponseEntity<Collaborator> getCollaborator(@RequestParam String userId,
                                                        @RequestParam String password,
                                                        @RequestParam String collaboratorId) {
        ResponseEntity<Collaborator> response;

        if (directoryService.collaboratorExists(collaboratorId)) {
            Collaborator collaborator = new Collaborator(collaboratorId);
            collaborator.Name(directoryService.getCollaboratorName(collaboratorId));
            collaborator.CallbackUrl(directoryService.getCallbackUrl(collaboratorId));

            response = new ResponseEntity<>(collaborator, HttpStatus.OK);
        }
        else {
            response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @PatchMapping
    public boolean updateCollaborator(@RequestParam String collaboratorId,
                                      @RequestParam String password,
                                      @RequestParam String keyStoreData,
                                      @RequestParam String urlCallback) {
        return true;
    }

    /*
    @DeleteMapping
    public boolean deleteCollaborator(@RequestParam String userId,
                                      @RequestParam String password,
                                      @RequestParam String address) {
        return true;
    }
    */
}
