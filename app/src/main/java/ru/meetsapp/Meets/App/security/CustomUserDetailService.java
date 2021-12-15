package ru.meetsapp.Meets.App.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.meetsapp.Meets.App.entity.User;
import ru.meetsapp.Meets.App.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    CustomUserDetailService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);

        if(user.isPresent()){
            return build(user.get());
        }
        return null;
    }


    public CustomUserDetail build(User user){
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : user.getRole()){
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);
            authorities.add(simpleGrantedAuthority);
        }
        return new CustomUserDetail(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

}
