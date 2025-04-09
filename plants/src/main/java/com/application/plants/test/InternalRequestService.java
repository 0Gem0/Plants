package com.application.plants.test;

import com.application.plants.test.ImageWithComponentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Service
public class InternalRequestService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public InternalRequestService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public ImageWithComponentDTO makeInternalPostRequest(Map<String, String> realCompsNames, String plantName, String compName) throws IOException {
        String url = "http://localhost:8080/api/plants/property?plant_name=" + plantName + "&comp_name=" + compName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = objectMapper.writeValueAsString(realCompsNames);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return objectMapper.readValue(response.getBody(), ImageWithComponentDTO.class);
        } else {
            throw new IOException("Request failed with status code: " + response.getStatusCode());
        }
    }
}
