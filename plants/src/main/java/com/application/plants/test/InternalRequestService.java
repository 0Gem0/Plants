package com.application.plants.test;

import com.application.plants.Parcing.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper; // Add Jackson for JSON conversion
import java.io.IOException;

@Service
public class InternalRequestService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;  // Inject ObjectMapper

    @Autowired
    public InternalRequestService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<byte[]> makeInternalPostRequest(Plant plant, String compName) throws IOException {
        String url = "http://localhost:8080/api/plants/property?comp_name=" + compName; // Add query param
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = objectMapper.writeValueAsString(plant); // Convert Plant to JSON

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForEntity(url, request, byte[].class); // Expecting byte array in response
    }
}
