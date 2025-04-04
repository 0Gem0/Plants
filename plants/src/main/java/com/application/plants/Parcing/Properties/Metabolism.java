package com.application.plants.Parcing.Properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Metabolism extends Property{
    public Metabolism(Property property) {
        super(property.name,property.activationProb,property.compound,property.AnotherProbs); // Передаем имя в конструктор Property
    }

    public Metabolism() {
    }

    @Override
    public String toString() {
        return "Metabolism{" +
                "name='" + name + '\'' +
                ", activationProb='" + activationProb + '\'' +
                ", compound='" + compound + '\'' +
                ", AnotherProbs='" + AnotherProbs + '\'' +
                '}';
    }
}
