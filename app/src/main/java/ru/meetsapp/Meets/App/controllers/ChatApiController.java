package ru.meetsapp.Meets.App.controllers;

import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ru.meetsapp.Meets.App.entity.CommentDraft;
import ru.meetsapp.Meets.App.repositories.CommentRedisRepository;

/**
 * ChatApiController
 */
@RestController
@RequestMapping("/api/comment")
public class ChatApiController {
    
    public static final Logger LOG = LoggerFactory.getLogger(ChatApiController.class);

    @Autowired
    CommentRedisRepository commentRedisRepository;

    @PostMapping("/save")
    public ResponseEntity<String> add(@RequestBody CommentDraft commentDraft) {
        
        commentRedisRepository.add(commentDraft);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{meetId}")
    public String getDraft (@PathVariable("meetId") long meetId) {
        UserDetails userDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = userDetails.getUsername() + ":" + Long.toString(meetId);
        
        String draft = commentRedisRepository.findCommentDraft(id);
        return draft;
 
    }
    
    
}