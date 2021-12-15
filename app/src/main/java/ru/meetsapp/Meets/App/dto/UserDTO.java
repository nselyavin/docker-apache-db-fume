package ru.meetsapp.Meets.App.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserDTO {
    public String name;
    public String lastname;
    @NotEmpty
    public String username;
    public String password;
    public String email;
    public Long userId;
    public String birthDay;
    public MultipartFile image;

}
