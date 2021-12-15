package ru.meetsapp.Meets.App.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDraft implements Serializable{
    String username;
    String meetId;
    String message;
    

    public String getId(){
        return username + ":" + meetId;
    }

    @Override
    public String toString() {
        return "CommentDraft{username = " + username + ", meetId = " + meetId + ", message = " + message + "}";
    }
}
