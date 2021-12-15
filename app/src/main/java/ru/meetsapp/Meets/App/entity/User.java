package ru.meetsapp.Meets.App.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@Table(name = "user")
public class User{
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String lastname;
    @Column(unique = true, updatable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false, length = 3000)
    private String password;
    @Column(columnDefinition = "INTEGER default 0")
    private int likes;
    @Column
    private String birthDay;

    @Column
    @ElementCollection(targetClass = Long.class, fetch = FetchType.EAGER)
    private Set<Long> likedUsers = new HashSet<>();
    @Column
    @ElementCollection(targetClass = Long.class, fetch = FetchType.EAGER)
    private Set<Long> bookmarkUsers = new HashSet<>();
    @Column
    @ElementCollection(targetClass = Long.class, fetch = FetchType.EAGER)
    private Set<Long> friends = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Bio bio;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "creator", orphanRemoval = true)
    private List<Meet> userMeets = new ArrayList<>();
    @Column
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> role = new HashSet<>();
    @JsonFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime createdDate;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    @PrePersist
    protected void onCreate(){
        this.createdDate = LocalDateTime.now();
    }

    public String getEncodedImage() {
        if(image!=null && image.length>0) {
            String encodeBase64 = Base64.getEncoder().encodeToString(image);
            return new String(encodeBase64);
        }
        else
            return "";
    }
}

