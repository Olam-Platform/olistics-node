package com.olam.node.service.application.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Shipment {
    private String name;
    private String state;
    private Collaborator[] collaborators;

    @JsonCreator
    public Shipment(@JsonProperty("name") String name,
                    @JsonProperty("state") String state,
                    @JsonProperty("collaborators") Collaborator[] collaborators)
    {
        Name(name);
        State(state);
        this.collaborators = collaborators;
    }

    @JsonGetter("name")
    public String Name() { return name; }
    public void Name(String name) { this.name = name; }

    @JsonGetter("state")
    public String State() { return state; }
    public void State(String state) { this.state = state; }

    @JsonGetter("collaborators")
    public Collaborator[] Collaborators() { return collaborators; }
    public void Collaborators(Collaborator[] collaborators) { this.collaborators = collaborators; }
}
