package com.application.plants.Parcing.Properties;


import lombok.Getter;
import lombok.Setter;


public class Antitargets extends Property {
    public Antitargets(Property property) {
        super(property.name,property.activationProb,property.compound,property.AnotherProbs); // Передаем имя в конструктор Property
    }

    public Antitargets() {

    }

    @Override
    public String toString() {
        return "Antitargets{" +
                "name='" + name + '\'' +
                ", activationProb='" + activationProb + '\'' +
                ", compound='" + compound + '\'' +
                ", AnotherProbs='" + AnotherProbs + '\'' +
                '}';
    }
}
