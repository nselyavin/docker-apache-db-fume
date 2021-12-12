package ru.meetsapp.Meets.App.dto;

import lombok.Getter;
import lombok.Setter;
import ru.meetsapp.Meets.App.entity.Meet;
import ru.meetsapp.Meets.App.entity.User;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CommentDTO {
    @NotNull
    public Long meetId;
    @NotNull
    public Long userId;
    @NotNull
    public String username;
    public String message;
}
