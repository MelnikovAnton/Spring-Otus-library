package ru.otus.library.secutity.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.library.secutity.model.UserEntity;

public interface UserRepo extends MongoRepository<UserEntity, String> {

    UserEntity findByUsername(String username);
}
