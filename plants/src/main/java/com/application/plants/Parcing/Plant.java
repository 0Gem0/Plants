package com.application.plants.Parcing;

import com.application.plants.Parcing.Properties.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Plant {

    private String name;
    private List<Antitargets> antitargets;
    private List<GeneExpression> geneExpressions;
    private List<Metabolism> metabolisms;
    private List<MechActivation> mechActivations;
    private List<PharmEff> pharmEffs;
    private List<PredictedActivities> predictedActivities;
    private List<ToxicEff> toxicEffs;
    private List<Transporters> transporters ;
    //key - comp.name ; value - path

    private Map<String,String> realCompsNames = new HashMap<>();

    private int status;

    public Plant(String name) {
        this.name = name;
    }
}
