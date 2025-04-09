package com.application.plants.Parcing.Properties;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ToxicEff extends Property{

    public ToxicEff(Property property) {
        super(property.name,property.activationProb,property.compound,property.getAnotherProbsParcered()); // Передаем имя в конструктор Property
    }

    public ToxicEff() {
    }

    @Override
    public String toString() {
        return "ToxicEff{" +
                "name='" + name + '\'' +
                ", activationProb='" + activationProb + '\'' +
                ", compound='" + compound + '\'' +
                ", AnotherProbs='" + AnotherProbs + '\'' +
                '}';
    }
}
