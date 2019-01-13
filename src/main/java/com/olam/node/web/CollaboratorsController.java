package com.olam.node.web;

import com.olam.node.service.application.EventsService;
import com.olam.node.service.application.entities.Collaborator;
import com.olam.node.service.application.entities.EventType;
import com.olam.node.service.application.entities.SubscribeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/collaborators")
public class CollaboratorsController {
    private static final Logger logger = LoggerFactory.getLogger(CollaboratorsController.class);

    @Autowired
    private EventsService eventsService;

    @PostMapping
    public boolean registerCollaborator(@RequestParam   String address,
                                        @RequestParam   String name,
                                        @RequestParam   String urlCallback) {
        return  true;
    }

    @GetMapping
    public ResponseEntity<Collaborator> getCollaborator(@RequestParam String address) {
        return new ResponseEntity(new Collaborator("Collaborator name", "Collaborator address"), HttpStatus.OK);
    }

    @PatchMapping
    public boolean updateCollaborator(@RequestParam String address,
                                      @RequestParam boolean connected,
                                      @RequestParam String urlCallback) {
        return true;
    }

    @DeleteMapping
    public boolean deleteCollaborator(@RequestParam String address) {
        return true;
    }
}
