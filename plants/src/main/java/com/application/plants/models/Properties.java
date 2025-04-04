//package com.application.plants.models;
//
//
//import com.application.plants.Parcing.Plant;
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//@Entity
//@Table(name = "properties")
//public class Properties {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @Column(name = "antitargets")
//    private String antitargets;
//
//    @OneToOne
//    @JoinColumn(name = "plant_id", referencedColumnName = "id")
//    @JsonBackReference
//    private Plant plant;
//}
