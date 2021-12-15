package ru.meetsapp.Meets.App.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bio")
public class Bio {
    @Id
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = true, columnDefinition = "FLOAT default 100")
    private Float height;
    @Column(nullable = true, columnDefinition = "FLOAT default 100")
    private Float weight;
    private String hairColor;
    private String gender;
    @Column(columnDefinition = "Text")
    private String biography;
    private String job;
    @Column(length = 512)
    private String specialSigns;
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

}
