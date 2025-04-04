package com.application.plants.Parcing.Properties;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PharmEff extends Property{

    public PharmEff(Property property) {
        super(property.name,property.activationProb,property.compound,property.AnotherProbs); // Передаем имя в конструктор Property
    }

    public PharmEff() {
    }

    @Override
    public String toString() {
        return "PharmEff{" +
                "name='" + name + '\'' +
                ", activationProb='" + activationProb + '\'' +
                ", compound='" + compound + '\'' +
                ", AnotherProbs='" + AnotherProbs + '\'' +
                '}';
    }
}
