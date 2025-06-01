package org.example.myspringsecurity.core.security;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.myspringsecurity.model.repository.UserRepository;
import org.example.myspringsecurity.securityLib.core.userDetails.UserDetails;
import org.example.myspringsecurity.securityLib.core.userDetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));
    }
}
