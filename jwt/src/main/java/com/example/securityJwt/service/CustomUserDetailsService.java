package com.example.securityJwt.service;

import com.example.securityJwt.dto.CustomUserDetails;
import com.example.securityJwt.entity.UserEntity;
import com.example.securityJwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // DB에서 조회
        UserEntity userData = userRepository.findByUsername(username);

        if (userData != null) {

            //UserDetails에 담아서 return하면 AuthenticationManager가 검증함
            return new CustomUserDetails(userData);
        }
        return null;
    }
}
