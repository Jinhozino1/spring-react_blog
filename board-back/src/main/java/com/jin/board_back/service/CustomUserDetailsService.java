package com.jin.board_back.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jin.board_back.config.CustomUserDetails;
import com.jin.board_back.entity.UserEntity;
import com.jin.board_back.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // System.out.println("🔍 Loading user by email: " + email);

        UserEntity user = userRepository.findByEmail(email); // 반환 타입: UserEntity
        if (user == null) {
            throw new UsernameNotFoundException("This user does not exist.");
        }

        return new CustomUserDetails(user);
    }
    
}
