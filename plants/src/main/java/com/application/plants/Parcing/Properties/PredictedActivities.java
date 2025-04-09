package com.application.plants.Parcing.Properties;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PredictedActivities extends Property{

    public PredictedActivities(Property property) {
        super(property.name,property.activationProb,property.compound,property.getAnotherProbsParcered()); // Передаем имя в конструктор Property
    }


    public PredictedActivities() {
    }

    @Override
    public String toString() {
        return "PredictedActivities{" +
                "name='" + name + '\'' +
                ", activationProb='" + activationProb + '\'' +
                ", compound='" + compound + '\'' +
                ", AnotherProbs='" + AnotherProbs + '\'' +
                '}';
    }
}
