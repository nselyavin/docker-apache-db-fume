package ru.meetsapp.Meets.App.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Data
@Entity
@Table(name = "meet")
public class Meet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "text", nullable = false)
    private String location;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime meetDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;
    @Column
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> meetUsers = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "meet", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    @Column(nullable = false, columnDefinition = "INTEGER default 0")
    private int open;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate(){
        this.createdDate = LocalDateTime.now();
    }

    public String getSDate(){
        String date = meetDateTime.toString();
        System.out.println(Arrays.toString(date.split("T")));
        date = date.split("T")[0];

        return date;
    }

    public String getSTime(){
        String time = meetDateTime.toString();
        time = time.split("T")[1];
        time = time.substring(0, 5);

        return time;
    }
}
