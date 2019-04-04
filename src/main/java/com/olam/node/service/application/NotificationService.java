package com.olam.node.service.application;


import com.olam.node.service.application.entities.Notification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;


public class NotificationService {
    public static boolean notify(String callbackUrl, Notification notification) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        LinkedMultiValueMap valueMap = new LinkedMultiValueMap();

        valueMap.add("shipmentId",  notification.ShipmentId());
        valueMap.add("sourceId", notification.SourceId());
        valueMap.add("targetId", notification.TargetId());

        HttpEntity<LinkedMultiValueMap> requestEntity = new HttpEntity<>(valueMap, requestHeaders);

        restTemplate.postForEntity(callbackUrl, requestEntity, String.class);

        return true;
    }
}
