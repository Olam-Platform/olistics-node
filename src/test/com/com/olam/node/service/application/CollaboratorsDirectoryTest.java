package com.olam.node.service.application;

import com.olam.node.service.application.entities.Collaborator;
import com.olam.node.service.infrastructure.storage.DirectoryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CollaboratorsDirectoryTest {
    DirectoryService directory = new DirectoryService();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addCollaborator() {
        Collaborator collaborator = new Collaborator("0x56F08C1046476D29236C3f4B0786923Bbb26a7a2", "Moshe");
        //directory.addCollaborator(collaborator);

        //directory.getCollaborator()
    }

    @Test
    public void getCollaborator() {
    }

    @Test
    public void updateCollaborator() {
    }

    @Test
    public void getCredentials() {
    }

    @Test
    public void getCallbackUrl() {
    }
}