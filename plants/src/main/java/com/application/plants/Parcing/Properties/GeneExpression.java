package com.application.plants.Parcing.Properties;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneExpression extends Property {

    public GeneExpression(Property property) {
        super(property.name,property.activationProb,property.compound,property.AnotherProbs); // Передаем имя в конструктор Property
    }

    public GeneExpression() {
    }

    @Override
    public String toString() {
        return "GeneExpression{" +
                "name='" + name + '\'' +
                ", activationProb='" + activationProb + '\'' +
                ", compound='" + compound + '\'' +
                ", AnotherProbs='" + AnotherProbs + '\'' +
                '}';
    }
}
