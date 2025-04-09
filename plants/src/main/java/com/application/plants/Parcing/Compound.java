package com.application.plants.Parcing;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Compound {
    private String activation;
    private String name;
    private String UIPACName;
    private String CHEMBLId;
    private String PubChemCID;
    private String CHEBI;
    private String MolecularFormula;
    private String MolecularWeight;
    private String StandardInChI;
    private String StandardInChIKey;
    private String SMILES;

    public Compound() {
    }

    public Compound(String activation, String name) {
        this.activation = activation;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Compound{" +
                "activation='" + activation + '\'' +
                ", name='" + name + '\'' +
                ", UIPACName='" + UIPACName + '\'' +
                ", CHEMBLId='" + CHEMBLId + '\'' +
                ", PubChemCID='" + PubChemCID + '\'' +
                ", CHEBI='" + CHEBI + '\'' +
                ", MolecularFormula='" + MolecularFormula + '\'' +
                ", MolecularWeight='" + MolecularWeight + '\'' +
                ", StandardInChI='" + StandardInChI + '\'' +
                ", StandardInChIKey='" + StandardInChIKey + '\'' +
                ", SMILES='" + SMILES + '\'' +
                '}';
    }
}
