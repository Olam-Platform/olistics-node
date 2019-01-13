package com.olam.node.service.application.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.net.URL;

public class Document {
    private String name;
    private URL url;
    private Collaborator owner;
    private Collaborator[] collaborators;

    @JsonCreator
    public Document(String name, URL url, Collaborator owner, Collaborator[] collaborators)
    {
        Name(name);
        Url(url);
        Owner(owner);
        Collaborators(collaborators);
    }

    @JsonGetter("name")
    public String Name() { return name; }
    public void Name(String name) { this.name = name; }

    @JsonGetter("url")
    public URL Url() { return url; }
    public void Url(URL state) { this.url = state; }

    @JsonGetter("owner")
    public Collaborator Owner() { return owner; }
    public void Owner(Collaborator owner) { this.owner = owner; }

    @JsonGetter("collaborators")
    public Collaborator[] Collaborators() { return collaborators; }
    public void Collaborators(Collaborator[] collaborators) { this.collaborators = collaborators; }
}
