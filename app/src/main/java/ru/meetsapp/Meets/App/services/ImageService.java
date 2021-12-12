//package ru.meetsapp.Meets.App.services;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import ru.meetsapp.Meets.App.entity.Image;
//import ru.meetsapp.Meets.App.entity.User;
//import ru.meetsapp.Meets.App.repositories.ImageRepository;
//import ru.meetsapp.Meets.App.repositories.MeetRepository;
//import ru.meetsapp.Meets.App.repositories.UserRepository;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOError;
//import java.io.IOException;
//import java.util.Optional;
//import java.util.zip.DataFormatException;
//import java.util.zip.Deflater;
//import java.util.zip.Inflater;
//
//@Service
//public class ImageService {
//    public static final Logger LOG = LoggerFactory.getLogger(ImageService.class);
//    private final ImageRepository imageRepository;
//    private final UserRepository userRepository;
//
//    @Autowired
//    public ImageService(ImageRepository repository, UserRepository userRepository){
//        this.imageRepository = repository;
//        this.userRepository = userRepository;
//    }
//
//    private byte[] compressBytes(byte[] data){
//        Deflater deflater = new Deflater();
//        deflater.setInput(data);
//        deflater.finish();
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
//        byte[] buffer = new byte[1024];
//        while(!deflater.finished()){
//            int count = deflater.deflate(buffer);
//            outputStream.write(buffer, 0, count);
//        }
//
//        try {
//            outputStream.close();
//        }catch (IOException e){
//            LOG.error("Cannot compress Bytes");
//        }
//        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
//        return outputStream.toByteArray();
//    }
//
//    private static byte[] decompressBytes(byte[] data){
//        Inflater inflater = new Inflater();
//        inflater.setInput(data);
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
//        byte[] buffer = new byte[1024];
//        try{
//            while(!inflater.finished()){
//                int count = inflater.inflate(buffer);
//                outputStream.write(buffer, 0, count);
//            }
//            outputStream.close();
//        }catch (IOException | DataFormatException e){
//            LOG.error("Cannot decompress bytes");
//        }
//        return outputStream.toByteArray();
//    }
//
//    public Image uploadImage(MultipartFile file, Long userId) throws IOException {
//        Optional<User> user = userRepository.findUserById(userId);
//        if(user.isEmpty()){
//            LOG.error("User not found id {}", userId);
//            return null;
//        }
//
//        Optional<Image> image = imageRepository.findImageByUserId(userId);
//        image.ifPresent(imageRepository::delete);
//
//        Image newImage = new Image();
//        newImage.setName("Profile_" + userId);
//        newImage.setUserId(userId);
//        newImage.setImageBytes(compressBytes(file.getBytes()));
//
//        try {
//            return imageRepository.save(newImage);
//        }
//        catch(Exception e){
//            LOG.error("Failed to save image for user {}", userId);
//            throw new RuntimeException("Failed to save image");
//        }
//    }
//
//    public Image getImageByUserId(Long userId){
//        Optional<User> user = userRepository.findUserById(userId);
//        if(user.isEmpty()){
//            LOG.error("Not found user");
//            return null;
//        }
//
//        Optional<Image> image = imageRepository.findImageByUserId(userId);
//        if(image.isPresent()){
//            image.get().setImageBytes(decompressBytes(image.get().getImageBytes()));
//            return image.get();
//        }
//        return null;
//    }
//}
