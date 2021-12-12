package ru.meetsapp.Meets.App.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.meetsapp.Meets.App.dto.BioDTO;
import ru.meetsapp.Meets.App.dto.UserDTO;
import ru.meetsapp.Meets.App.entity.User;
import ru.meetsapp.Meets.App.services.MeetService;
import ru.meetsapp.Meets.App.services.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class UserProfileController {
    public static final Logger LOG = LoggerFactory.getLogger(MeetService.class);

    @Autowired
    UserService userService;

    public void setupModel(Model model){
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        UserDTO userDTO = new UserDTO();
        BioDTO bioDTO = new BioDTO();
        model.addAttribute("user", user);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("bioDTO", bioDTO);
        model.addAttribute("bio", user.getBio());
        model.addAttribute("userDetails", userDetails);

    }

    @GetMapping("")
    public String userProfile(Model model){

        setupModel(model);
        return "profile";
    }

    @PostMapping("/upload")
    public String uploadImage(@Valid UserDTO userDTO, Model model){

        String error = "";

        try {
            if(!userDTO.image.isEmpty()) {
                userService.uploadImage(userDTO.username, userDTO.image);
            }else{
                error = "Select image";
            }
        }catch (IOException e){
            LOG.error("Failed to upload image: {}", e.getMessage());
            error = "Failed to load image: " + e.getMessage();
        }
        model.addAttribute("error", error);
        setupModel(model);
        return "redirect:/profile";
    }

    @PutMapping("/updateBio")
    public String updateBio(@Valid BioDTO bioDTO, Model model){

        userService.updateBio(bioDTO.getUsername(), bioDTO);

        setupModel(model);
        return "redirect:/profile";
    }

}
