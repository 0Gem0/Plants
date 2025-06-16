package com.application.plants.controllers;


import com.application.plants.Parcing.Compound;
import com.application.plants.Parcing.Parcer;
import com.application.plants.Parcing.Plant;


import com.application.plants.test.SomeOtherClass;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class PlantsController {
    private SomeOtherClass someOtherClass;
    private Parcer parcer;

    @Autowired
    public PlantsController(SomeOtherClass someOtherClass, Parcer parcer) {
        this.someOtherClass = someOtherClass;
        this.parcer = parcer;
    }

    @GetMapping("/plants")
    public ResponseEntity<Object> showPlant(@RequestParam(value = "plant_name") String plantName,
                                            @RequestParam(value = "property") String property) {
        Plant plant = new Plant(plantName);
        Optional<JsonNode> json = parcer.getInfo(plant, plantName, property);
        return json.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(plant));
    }
    @GetMapping("/plants/names")
    public List<String> showPlants(){
        return parcer.getNames();
    }

    @PostMapping("/plants/property")
    public ResponseEntity<?> showComp(@RequestBody Map<String,String> realCompsNames, @RequestParam(value = "plant_name") String plantName,@RequestParam(value = "comp_name") String compName) {
        try {
            byte[] bytes = parcer.imageComp(compName,plantName ,realCompsNames);
            if (bytes == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("Изображение не найдено для компонента: " + compName);
            }

            Compound component = parcer.getCompoundWrapper(realCompsNames,plantName,compName);

            String base64Image = Base64.getEncoder().encodeToString(bytes);

            Map<String, Object> response = new HashMap<>();
            response.put("compound", component);
            response.put("imageBase64", base64Image);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Ошибка сервера при генерации изображения: " + e.getMessage());
        }
    }

    @GetMapping("/trigger-request")
    public String triggerRequest() throws IOException {
        someOtherClass.someMethod();
        return "Request triggered!";
    }
}
