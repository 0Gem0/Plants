package com.application.plants.controllers;


import com.application.plants.Parcing.Parcer;
import com.application.plants.Parcing.Plant;


import com.application.plants.test.SomeOtherClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
    public Plant showPlant(@RequestParam(value = "plant_name") String plantName, @RequestParam(value = "property") String property){
        Plant plant = new Plant(plantName);
        parcer.getInfo(plant,plantName,property);
        return plant;
    }
    @GetMapping("/plants/names")
    public List<String> showPlants(){
        return parcer.getNames();
    }

    @PostMapping("/plants/property")
    public ResponseEntity<?> showComp(@RequestBody Plant plant, @RequestParam(value = "comp_name") String compName) {
        System.out.println(plant.getGeneExpressions());
        try {
            byte[] bytes = parcer.imageComp(compName, plant);
            if (bytes == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("Изображение не найдено для компонента: " + compName);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(bytes.length);

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
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
