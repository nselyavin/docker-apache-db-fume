package ru.meetsapp.Meets.App.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private long userId;
    @Column(nullable = false)
    private String username;
    @ManyToOne(fetch = FetchType.EAGER)
    private Meet meet;
    @Column(columnDefinition = "text", nullable = false)
    private String message;

    @JsonFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate(){
        this.createdDate = LocalDateTime.now();
    }
}
