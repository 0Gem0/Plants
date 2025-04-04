package com.application.plants.test;

import com.application.plants.Parcing.Parcer;
import com.application.plants.Parcing.Plant;
import com.application.plants.test.InternalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SomeOtherClass {

    private Parcer parcer;
    private final InternalRequestService internalRequestService;

    @Autowired
    public SomeOtherClass(Parcer parcer, InternalRequestService internalRequestService) {
        this.parcer = parcer;
        this.internalRequestService = internalRequestService;
    }

    public void someMethod() throws IOException {
        String property = "Mechanisms of Action";
        String plantName = "Bergenia crassifolia";
        Plant plant = new Plant(plantName);
        parcer.getInfo(plant,plantName,property); // Create a Plant object
        String compName = "Kinidilin";

        ResponseEntity<byte[]> response = internalRequestService.makeInternalPostRequest(plant, compName);

        if (response.getStatusCode() == HttpStatus.OK) {
            byte[] imageBytes = response.getBody();
            // Process the imageBytes
            System.out.println("Received image with length: " + imageBytes.length);
        } else {
            System.err.println("Request failed with status code: " + response.getStatusCode());
        }
    }
}
