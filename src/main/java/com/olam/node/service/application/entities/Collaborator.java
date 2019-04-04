package com.olam.node.service.application.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Collaborator {
    private String name = null;
    private String role = null;
    private String publicAddress = null;
    private String callbackUrl = null;

    @JsonCreator
    public Collaborator(@JsonProperty("address") String publicAddress,
                        @JsonProperty("name") String name,
                        @JsonProperty("role") String role,
                        @JsonProperty("callbackurl") String callbackUrl) {
        Name(name);
        Role(role);
        Address(publicAddress);
    }

    @JsonCreator
    public Collaborator(@JsonProperty("address") String publicAddress,
                        @JsonProperty("name") String name,
                        @JsonProperty("role") String role) {
        Name(name);
        Role(role);
        Address(publicAddress);
    }

    @JsonCreator
    public Collaborator(@JsonProperty("address") String publicAddress,
                        @JsonProperty("name") String name) {
        Name(name);
        Address(publicAddress);
    }

    @JsonCreator
    public Collaborator(@JsonProperty("address")    String publicAddress) {
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

    @JsonGetter("callbackurl")
    public String CallbackUrl() { return callbackUrl; }
    public void CallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }
}
