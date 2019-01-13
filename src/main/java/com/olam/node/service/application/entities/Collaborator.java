package com.olam.node.service.application.entities;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Collaborator {
    private String name;
    private String role;
    private String publicAddress;

    @JsonCreator
    public Collaborator(@JsonProperty("name")       String name,
                        @JsonProperty("role")       String role,
                        @JsonProperty("address")    String publicAddress) {
        Name(name);
        Role(role);
        Address(publicAddress);
    }

    @JsonCreator
    public Collaborator(@JsonProperty("name")       String name,
                        @JsonProperty("address")    String publicAddress) {
        Name(name);
        Role("unassigned");
        Address(publicAddress);
    }

    @JsonCreator
    public Collaborator(@JsonProperty("address")    String publicAddress) {
        Name("unassigned");
        Role("unassigned");
        Address(publicAddress);
    }

    @JsonGetter("name")
    public String Name() { return name; }
    public void   Name(String name) { this.name = name; }

    @JsonGetter("role")
    public String Role() { return role; }
    public void   Role(String role) { this.role = role; }

    @JsonGetter("address")
    public String Address() { return publicAddress; }
    public void Address(String publicAddress) { this.publicAddress = publicAddress; }
}
