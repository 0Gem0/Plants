package com.application.plants.Parcing.Properties;

public class MechActivation extends Property{

    public MechActivation(Property property) {
        super(property.name,property.activationProb,property.compound,property.getAnotherProbsParcered()); // Передаем имя в конструктор Property
    }

    public MechActivation() {
    }

    @Override
    public String toString() {
        return "MechActivation{" +
                "name='" + name + '\'' +
                ", activationProb='" + activationProb + '\'' +
                ", compound='" + compound + '\'' +
                ", AnotherProbs='" + AnotherProbs + '\'' +
                '}';
    }
}
