package com.application.plants.Parcing.Properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transporters extends Property{
    public Transporters(Property property) {
        super(property.name,property.activationProb,property.compound,property.AnotherProbs); // Передаем имя в конструктор Property
    }

    public Transporters() {
    }

    @Override
    public String toString() {
        return "Transporters{" +
                "name='" + name + '\'' +
                ", activationProb='" + activationProb + '\'' +
                ", compound='" + compound + '\'' +
                ", AnotherProbs='" + AnotherProbs + '\'' +
                '}';
    }
}
