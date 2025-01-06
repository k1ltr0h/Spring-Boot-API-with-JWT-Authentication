package com.jwt.project.jwtauthentication.Services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.jwt.project.jwtauthentication.entity.User;
import com.jwt.project.jwtauthentication.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username)
                // if useename not avalble it will throws error
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found !!!"));
        return user;
    }

}
