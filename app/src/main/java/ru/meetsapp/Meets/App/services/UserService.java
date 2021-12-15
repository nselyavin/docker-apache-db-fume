package ru.meetsapp.Meets.App.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.meetsapp.Meets.App.dto.BioDTO;
import ru.meetsapp.Meets.App.dto.UserDTO;
import ru.meetsapp.Meets.App.entity.Bio;
import ru.meetsapp.Meets.App.entity.User;
import ru.meetsapp.Meets.App.entity.enums.Roles;
import ru.meetsapp.Meets.App.repositories.BioRepository;
import ru.meetsapp.Meets.App.repositories.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final BioRepository bioRepository;

    @Autowired
    public UserService(UserRepository repository, BCryptPasswordEncoder encoder, BioRepository bioRepository){
        this.repository = repository;
        this.encoder = encoder;
        this.bioRepository = bioRepository;
    }

    public User createUser(UserDTO dto){
        User newUser = new User();
        newUser.setUsername(dto.username);
        newUser.setName(dto.name);
        newUser.setLastname(dto.lastname);
        newUser.setEmail(dto.email);
        newUser.setPassword(encoder.encode(dto.password));
        newUser.setBirthDay(dto.birthDay);
        newUser.getRole().add(Roles.ROLE_USER);
        newUser.setImage(null);

        try {
            LOG.info("Saving user {}", dto.username);
            return repository.save(newUser);
        } catch (Exception e){
            LOG.error("Error during creating user {}", e.getMessage());
            throw new RuntimeException("Failed create user");
        }
    }

    public User getUserById(Long id){
        Optional<User> user = repository.findUserById(id);
        if(user.isEmpty()){
            LOG.error("Error not found user, id {}", id);
            return null;
        }
        user.get().setImage(decompressBytes(user.get().getImage()));

        return user.get();
    }

    public User getUserByUsername(String username){
        Optional<User> user = repository.findUserByUsername(username);
        if(user.isEmpty()){
            LOG.error("Error not found user, id {}", username);
            return null;
        }

        user.get().setImage(decompressBytes(user.get().getImage()));

        return user.get();
    }

    public List<User> getLastUsers(int amount){
        List<User> users = repository.findAllByOrderByCreatedDateDesc();

        List<User> result = new ArrayList<>();
        for (int i = 0; i < amount && i < users.size(); i++){
            User user = users.get(i);
            user.setImage(decompressBytes(user.getImage()));
            result.add(user);
        }

        return result;
    }


    public Set<Long> getBookmarksIdById(Long id){
        Optional<User> user = repository.findUserById(id);
        if(user.isEmpty()){
            LOG.error("Error not bookmark id {}", id);
            return null;
        }

        return user.get().getBookmarkUsers();
    }


    public Set<Long> getLikedUsersIdById(Long id) {
        Optional<User> user = repository.findUserById(id);
        if(user.isEmpty()){
            LOG.error("Error not found liked, id {}", id);
            return null;
        }

        return user.get().getLikedUsers();
    }

    public User bookmarkUser(Long id, Long bookmarkId){
        Optional<User> user = repository.findUserById(id);
        if(user.isEmpty()){
            LOG.error("Error not found user, id {}", id);
            return null;
        }

        Set<Long>  bookmarkUsers = user.get().getBookmarkUsers();
        if(!bookmarkUsers.contains(bookmarkId)){
            bookmarkUsers.add(bookmarkId);
        }else{
            bookmarkUsers.remove(bookmarkId);
        }

        try {
            return repository.save(user.get());
        } catch (Exception e){
            LOG.error("Failed to bookmark user with {} id", bookmarkId);
            throw new RuntimeException("Failed to bookmark user");
        }
    }

    public User likeUser(Long id, Long likeId){
        Optional<User> user = repository.findUserById(id);
        if(user.isEmpty()){
            LOG.error("Error not found user, id {}", id);
            return null;
        }

        Set<Long>  likedUsers = user.get().getLikedUsers();
        if(!likedUsers.contains(likeId)){
            likedUsers.add(likeId);
        }else{
            likedUsers.remove(likeId);
        }

        try {
            return repository.save(user.get());
        } catch (Exception e){
            LOG.error("Failed to like user with {} id", likeId);
            throw new RuntimeException("Failed to like user");
        }
    }

    public User updateBio(String username, BioDTO bioDto){
        Optional<User> user = repository.findUserByUsername(username);
        if(user.isEmpty()){
            LOG.error("Error not found user {}", username);
            return null;
        }

        Optional<Bio> founded = bioRepository.findBioByUser(user.get());
        Bio newBio;
        if(founded.isEmpty()){
            newBio = new Bio();
        }else{
            newBio = founded.get();
        }
        newBio.setBiography(bioDto.biography);
        if(bioDto.gender != null) {
            newBio.setGender(bioDto.gender);
        }
        newBio.setHairColor(bioDto.hairColor);
        newBio.setWeight(bioDto.weight);
        if(newBio.getWeight() == null){
            newBio.setWeight(100f);
        }
        newBio.setHeight(bioDto.height);
        if(newBio.getHeight() == null){
            newBio.setHeight(100f);
        }
        newBio.setJob(bioDto.job);
        newBio.setSpecialSigns(bioDto.specialSigns);
        newBio.setUser(user.get());

        user.get().setBio(newBio);

        try {
            return repository.save(user.get());
        } catch (Exception e){
            LOG.error("Failed to update bio of user {}: {}", username, e.getMessage());
            throw new RuntimeException("Failed to update bio");
        }
    }

    public void deleteUserById(Long id){
        Optional<User> meet = repository.findUserById(id);
        meet.ifPresent(repository::delete);
    }

    public void deleteUserByUsername(String username){
        Optional<User> meet = repository.findUserByUsername(username);
        meet.ifPresent(repository::delete);
    }

    private byte[] compressBytes(byte[] data){
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while(!deflater.finished()){
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        try {
            outputStream.close();
        }catch (IOException e){
            LOG.error("Cannot compress Bytes");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data){
        if (data == null){
            return null;
        }
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try{
            while(!inflater.finished()){
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        }catch (IOException | DataFormatException e){
            LOG.error("Cannot decompress bytes");
        }
        return outputStream.toByteArray();
    }

    public User uploadImage(String username, MultipartFile file) throws IOException {
        Optional<User> user = repository.findUserByUsername(username);
        if(user.isEmpty()){
            LOG.error("User not found id {}", username);
            return null;
        }

        user.get().setImage(compressBytes(file.getBytes()));

        try {
            return repository.save(user.get());
        }
        catch(Exception e){
            LOG.error("Failed to save image for user {}", username);
            throw new RuntimeException("Failed to save image");
        }
    }

}
