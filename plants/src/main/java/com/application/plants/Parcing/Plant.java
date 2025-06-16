package com.application.plants.Parcing;

import com.application.plants.Parcing.Properties.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Plant {

    @JsonIgnore
    private String name;

    @JsonProperty("Antitargets")
    private List<Antitargets> antitargets;

    @JsonProperty("Gene Expression Regulation")
    private List<GeneExpression> geneExpressions;

    @JsonProperty("Metabolism-Related Actions")
    private List<Metabolism> metabolisms;

    @JsonProperty("Mechanisms of Action")
    private List<MechActivation> mechActivations;

    @JsonProperty("Pharmacological Effects")
    private List<PharmEff> pharmEffs;

    @JsonProperty("Predicted Activities")
    private List<PredictedActivities> predictedActivities;

    @JsonProperty("Toxic and Adverse Effects")
    private List<ToxicEff> toxicEffs;

    @JsonProperty("Transporters-Related Actions")
    private List<Transporters> transporters ;

    private Map<String,String> realCompsNames = new HashMap<>();

    private int status;

    public Plant(String name) {
        this.name = name;
    }
}
