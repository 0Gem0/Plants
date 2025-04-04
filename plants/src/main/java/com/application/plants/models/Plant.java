//package com.application.plants.models;
//
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import org.hibernate.annotations.Cascade;
//import org.springframework.stereotype.Service;
//
//
//@Getter
//@Setter
//@Entity
//@Table(name = "plants")
//public class Plant {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @Column(name = "name")
//    private String name;
//
//    @OneToOne(mappedBy = "plant")
//    @JsonManagedReference
//    private Properties properties;
//}
