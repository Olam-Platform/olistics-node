package com.olam.node.sdk;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class IPFSCluster {
    private final String ID = "id";
    private final String VERSION = "version";
    private final String PINS = "pins";
    private final String PEERS = "peers";
    private final String ALLOCATIONS = "allocations";
    private final String SYNC = "sync";
    private final String RECOVER = "recover";
    private final String HEALTH = "health";
    private final String GRAPH = "graph";


    private RestTemplate restClient;
    private String host;
    private int port;
    private String scheme;
    public final Pins pins;
    public final Peers peers;
    public final Allocations allocations;
    public final Health health;

    public IPFSCluster(String host, int port, String scheme) {
        this.restClient = new RestTemplate();
        this.pins = new Pins();
        this.peers = new Peers();
        this.allocations = new Allocations();
        this.health = new Health();
        this.host = host;
        this.port = port;
        this.scheme = scheme;
    }


    public IPFSCluster(String host, int port) {
        this(host,port,"http");
    }

    private String buildUri(String... path) {
        return UriComponentsBuilder.newInstance()
                .scheme(scheme).host(host).port(port).pathSegment(path).build().toUriString();
    }

    public ResponseEntity<String> id() {
        String uri = buildUri(ID);
        return restClient.getForEntity(uri, String.class);
    }

    public ResponseEntity<String> version() {
        String uri = buildUri(VERSION);
        return restClient.getForEntity(uri, String.class);
    }

    public class Pins {
        public ResponseEntity<String> status() {
            String uri = buildUri(PINS);
            return restClient.getForEntity(uri, String.class);
        }

        public ResponseEntity<String> status(String cid) {
            String url = buildUri(PINS, cid);
            return restClient.getForEntity(url, String.class);
        }

        public ResponseEntity<String> sync() {
            String url = buildUri(PINS, SYNC);
            return restClient.postForEntity(url, null, String.class);
        }


        public ResponseEntity<String> sync(String cid) {
            String url = buildUri(PINS, cid, SYNC);
            return restClient.getForEntity(url, String.class);
        }

        public void delete(String cid) {
            String url = buildUri(PINS, cid);
            restClient.delete(url);
        }

        public ResponseEntity<String> add(String cid) {
            String url = buildUri(PINS, cid);
            return restClient.postForEntity(url, null, String.class);
        }

        public ResponseEntity<String> recover(String cid) {
            String url = buildUri(PINS, cid, RECOVER);
            return restClient.postForEntity(url, null, String.class);
        }

        public ResponseEntity<String> recover() {
            String url = buildUri(PINS, RECOVER);
            return restClient.postForEntity(url, null, String.class);
        }
    }

    public class Peers {
        public ResponseEntity<String> list() {
            String uri = buildUri(PEERS);
            return restClient.getForEntity(uri, String.class);
        }

        public void remove(String peerId) {
            String uri = buildUri(PEERS, peerId);
            restClient.delete(uri);
        }
    }

    public class Allocations {
        public ResponseEntity<String> list() {
            String uri = buildUri(ALLOCATIONS);
            return restClient.getForEntity(uri, String.class);
        }

        public void list(String cid) {
            String uri = buildUri(PEERS, cid);
            restClient.delete(uri);
        }

    }

    public class Health {
        public ResponseEntity<String> graph() {
            String uri = buildUri(HEALTH, GRAPH);
            return restClient.getForEntity(uri, String.class);
        }
    }
}
