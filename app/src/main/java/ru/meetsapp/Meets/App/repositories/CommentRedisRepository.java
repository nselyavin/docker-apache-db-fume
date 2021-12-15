package ru.meetsapp.Meets.App.repositories;


import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.xml.stream.events.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.meetsapp.Meets.App.entity.CommentDraft;

@Repository
public class CommentRedisRepository{
    
    private static final String KEY = "CommentDraft";
    
    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;
    
    @Autowired
    public CommentRedisRepository(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }
    
    public void add(final CommentDraft commentDraft) {
        hashOperations.put(KEY, commentDraft.getId(), commentDraft.getMessage());
    }

    public void delete(final String id) {
        hashOperations.delete(KEY, id);
    }
    
    public String findCommentDraft(final String id){
        return (String) hashOperations.get(KEY, id);
    }
}
