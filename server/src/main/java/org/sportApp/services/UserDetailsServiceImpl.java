package org.sportApp.services;

import org.sportApp.repo.UserRepository;
import org.sportApp.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = this.repository.getByLogin(username);
        if(userEntity == null) {
            throw new UsernameNotFoundException("Unknown user " + username);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(userEntity.getLogin())
                .password(userEntity.getPassword())
                .authorities("ROLE_USER")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

}