package com.application.plants.Parcing.Properties;


import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Property {
    String name;
    String activationProb;
    String compound;
    String AnotherProbs;
    public Property(){

    }
    public Property(String name, String activationProb, String compound, String anotherProbs) {
        this.name = name;
        this.activationProb = activationProb;
        this.compound = compound;
        AnotherProbs = anotherProbs;
    }
}
