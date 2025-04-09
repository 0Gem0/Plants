package com.application.plants.Parcing.Properties;


import com.application.plants.Parcing.Compound;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Property {
    String name;
    String activationProb;
    String compound;
    String AnotherProbs;
    List<Compound> anotherProbsParcered;
    public Property(){

    }
    public Property(String name, String activationProb, String compound, String anotherProbs) {
        this.name = name;
        this.activationProb = activationProb;
        this.compound = compound;
        this.AnotherProbs = anotherProbs;
    }

    public Property(String name, String activationProb, String compound, List<Compound> anotherProbsParcered) {
        this.name = name;
        this.activationProb = activationProb;
        this.compound = compound;
        this.anotherProbsParcered = anotherProbsParcered;
    }
}
