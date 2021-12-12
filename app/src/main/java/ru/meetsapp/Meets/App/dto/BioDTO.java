package ru.meetsapp.Meets.App.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Getter
@Setter
public class BioDTO {
    public Float height;
    public Float weight;
    public String hairColor;
    public String gender;
    public String biography;
    public String job;
    public String specialSigns;
    public String name;
    public String lastname;
    @NotNull
    public String username;
    public String birthday;
}
