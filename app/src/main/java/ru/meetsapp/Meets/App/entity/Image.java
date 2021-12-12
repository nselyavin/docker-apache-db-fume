//package ru.meetsapp.Meets.App.entity;
//
//import lombok.Data;
//import net.minidev.json.annotate.JsonIgnore;
//
//import javax.persistence.*;
//
//@Data
//@Entity
//@Table(name = "image")
//public class Image {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @Column(nullable = false)
//    private String name;
//    @Lob
//    @Column(columnDefinition = "LONGBLOB")
//    private byte[] imageBytes;
//    @JsonIgnore
//    private long userId;
//}
