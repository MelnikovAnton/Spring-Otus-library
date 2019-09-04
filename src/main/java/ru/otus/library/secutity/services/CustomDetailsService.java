package ru.otus.library.secutity.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.library.secutity.model.CustomUser;
import ru.otus.library.secutity.model.UserEntity;
import ru.otus.library.secutity.repo.UserRepo;
import ru.otus.library.util.CustomLog;

@Service
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @CustomLog
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepo.findByUsername(username);
        return new CustomUser(userEntity);
    }
}
