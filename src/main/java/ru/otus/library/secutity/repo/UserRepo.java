package ru.otus.library.secutity.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.library.secutity.model.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);
}
