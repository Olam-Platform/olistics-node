package com.olam.node.service.application.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;

public class Document {
    private String name;
    private String url;
    private Collaborator owner;
    private Collaborator collaborators;

    @JsonCreator
    public Document(String name, String url, Collaborator owner, Collaborator collaborators)
    {
        Name(name);
        Url(url);
        Owner(owner);
        Collaborator(collaborators);
    }

    @JsonGetter("name")
    public String Name() { return name; }
    public void Name(String name) { this.name = name; }

    @JsonGetter("url")
    public String Url() { return url; }
    public void Url(String state) { this.url = state; }

    @JsonGetter("owner")
    public Collaborator Owner() { return owner; }
    public void Owner(Collaborator owner) { this.owner = owner; }

    @JsonGetter("collaborators")
    public Collaborator Collaborator() { return collaborators; }
    public void Collaborator(Collaborator collaborators) { this.collaborators = collaborators; }
}
