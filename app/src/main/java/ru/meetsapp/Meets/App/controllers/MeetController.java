package ru.meetsapp.Meets.App.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.meetsapp.Meets.App.dto.BioDTO;
import ru.meetsapp.Meets.App.dto.CommentDTO;
import ru.meetsapp.Meets.App.dto.MeetDTO;
import ru.meetsapp.Meets.App.dto.UserDTO;
import ru.meetsapp.Meets.App.entity.Comment;
import ru.meetsapp.Meets.App.entity.Meet;
import ru.meetsapp.Meets.App.entity.User;
import ru.meetsapp.Meets.App.services.CommentService;
import ru.meetsapp.Meets.App.services.MeetService;
import ru.meetsapp.Meets.App.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/meets")
public class MeetController {
    @Autowired
    UserService userService;

    @Autowired
    MeetService meetService;

    @Autowired
    CommentService commentService;

    public void setupModel(Model model){
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", user);
        model.addAttribute("userDetails", userDetails);
    }

    @GetMapping("")
    public String meetsPage(Model model){
        List<Meet> meets = meetService.getMeetList(20);
        model.addAttribute("meets", meets);
        setupModel(model);
        return "meets";
    }

    @GetMapping("my")
    public String myMeetsPage(Model model){
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        List<Meet> meets = meetService.getMeetListByUser(user, 10);
        model.addAttribute("meets", meets);
        model.addAttribute("my", "my");
        setupModel(model);
        return "meets";
    }

    @GetMapping("/new")
    public String newMeetPage(Model model){
        MeetDTO meetDTO = new MeetDTO();
        model.addAttribute("meetDTO", meetDTO);
        setupModel(model);
        return "meet_new";
    }

    @PostMapping("/new")
    public String newMeet(@Valid MeetDTO meetDTO,  Model model){
        meetService.createMeet(meetDTO);
        setupModel(model);
        return "redirect:/meets";
    }

    @GetMapping("/{id}")
    public String viewMeetPage(@PathVariable("id") long meetId, Model model){
        Meet meet = meetService.getMeetListById(meetId);
        List<Comment> comments = meet.getComments();
        Set<String> meetUsers = meet.getMeetUsers();
        CommentDTO commentDTO = new CommentDTO();
        model.addAttribute("meet", meet);
        model.addAttribute("comments", comments);
        model.addAttribute("meetUsers", meetUsers);
        model.addAttribute("addUsers", "");
        model.addAttribute("commentDTO", commentDTO);
        model.addAttribute("meetDTO", new MeetDTO());
        setupModel(model);
        return "meet_view";
    }

    @PostMapping("/{id}/addUsers")
    public String viewMeetAddUsers(@RequestParam String addUsers, @PathVariable("id") long meetId, Model model){
        for(String username : addUsers.split(" ")) {
            meetService.addUserToMeet(meetId, username);
        }
        setupModel(model);
        return "redirect:/meets/" + Long.toString(meetId);
    }

    @PostMapping("/{id}/addComment")
    public String viewMeetAddUsers(@Valid CommentDTO commentDTO, @PathVariable("id") long meetId, Model model){
        commentService.addComment(commentDTO);
        setupModel(model);
        return "redirect:/meets/" + Long.toString(meetId);
    }

    @DeleteMapping("/{id}")
    public String deleteMeet(@PathVariable("id") long meetId, Model model){
        meetService.deleteMeetById(meetId);
        setupModel(model);
        return "redirect:/meets";
    }


}
