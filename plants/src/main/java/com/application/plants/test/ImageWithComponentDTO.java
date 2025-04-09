package com.application.plants.test;

import com.application.plants.Parcing.Compound;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageWithComponentDTO {
    private Compound compound;
    private String imageBase64;
}
