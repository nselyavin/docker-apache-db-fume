package ru.meetsapp.Meets.App.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.meetsapp.Meets.App.dto.MeetDTO;
import ru.meetsapp.Meets.App.entity.Meet;
import ru.meetsapp.Meets.App.entity.User;
import ru.meetsapp.Meets.App.repositories.MeetRepository;
import ru.meetsapp.Meets.App.repositories.UserRepository;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MeetService {
    public static final Logger LOG = LoggerFactory.getLogger(MeetService.class);
    private final MeetRepository meetRepository;
    private final UserRepository userRepository;

    @Autowired
    public MeetService(MeetRepository meetRepository, UserRepository userRepository){
        this.meetRepository = meetRepository;
        this.userRepository = userRepository;
    }

    public Meet createMeet(MeetDTO dto){
        Optional<User> user = userRepository.findUserByUsername(dto.getCreator());
        if(user.isEmpty()){
            LOG.error("User {} not found", dto.getCreator());
            return null;
        }

        Meet newMeet = new Meet();
        newMeet.setTitle(dto.title);
        StringBuilder fullTime = new StringBuilder();
        fullTime.append(dto.meetDate).append(" ").append(dto.meetTime).append(":00");
        LocalDateTime dateTime = LocalDateTime.parse(fullTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        newMeet.setMeetDateTime(dateTime);
        newMeet.getMeetUsers().add(user.get().getUsername());
        newMeet.setCreator(user.get());
        newMeet.setLocation(dto.location);
        newMeet.setOpen(dto.open  ? 1 : 0);

        try {
            LOG.info("Saving meet {}", dto.title);
            return meetRepository.save(newMeet);
        } catch (Exception e){
            LOG.error("Error during creating meet {}", e.getMessage());
            throw new RuntimeException("The meet cannot create");
        }
    }

    public Meet addUserToMeet(Long id, String username){
        Optional<User> user = userRepository.findUserByUsername(username);
        if(user.isEmpty()){
            LOG.warn("User {} not found", username);
            return null;
        }

        Optional<Meet> meet = meetRepository.findMeetById(id);

        if(meet.isEmpty()){
            LOG.error("Not found meet");
            return null;
        }

        meet.get().getMeetUsers().add(user.get().getUsername());
        return meetRepository.save(meet.get());
    }

    public List<Meet> getMeetListByCreator(User creator, int amount){
        List<Meet> meets = meetRepository.findAllByCreatorOrderByCreatedDateDesc(creator);
        List<Meet> result = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            result.add(meets.get(i));
        }

        return result;
    }

    public List<Meet> getMeetList(int amount){
        List<Meet> meets = meetRepository.findAllByOrderByCreatedDateDesc();
        List<Meet> result = new ArrayList<>();
        for(int i = 0; i < amount && i < meets.size(); i++){
            result.add(meets.get(i));
        }

        return result;
    }

    public List<Meet> getMeetListByUser(User user, int amount){
        List<Meet> meets = meetRepository.findAllByMeetUsersLikeOrderByCreatedDateDesc(user.getUsername());
        List<Meet> result = new ArrayList<>();
        for(int i = 0; i < amount && i < meets.size(); i++){
            result.add(meets.get(i));
        }

        return result;
    }

    public void deleteMeetById(Long id){
        Optional<Meet> meet = meetRepository.findMeetById(id);
        meet.get().getMeetUsers().clear();
        meetRepository.delete(meet.get());
    }

    public Meet getMeetListById(long meetId) {
        Optional<Meet> meet = meetRepository.findMeetById(meetId);
        if(meet.isEmpty()){
            LOG.error("Meet {} not found", meetId);
            return null;
        }

        return meet.get();
    }
}
