package ru.meetsapp.Meets.App.dto;

import lombok.Getter;
import lombok.Setter;
import ru.meetsapp.Meets.App.entity.User;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
public class MeetDTO {
    public Long id;
    public String title;
    public String location;
    public String creator;
    public String meetDate;
    public String meetTime;
    public boolean open = false;
}
