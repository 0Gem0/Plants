package com.application.plants.test;

import com.application.plants.Parcing.Compound;
import com.application.plants.Parcing.Parcer;
import com.application.plants.Parcing.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

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
        parcer.getInfo(plant, plantName, property);

        String compName = "Kinidilin";

        ImageWithComponentDTO result = internalRequestService.makeInternalPostRequest(plant.getRealCompsNames(), plantName, compName);

        Compound compound = result.getCompound();
        String base64 = result.getImageBase64();

        if (base64 != null && !base64.isEmpty()) {
            byte[] imageBytes = Base64.getDecoder().decode(base64);
            System.out.println("Image length: " + imageBytes.length);
        }

        System.out.println("Component info: " + compound);
    }
}
