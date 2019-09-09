package ru.otus.library.security.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.library.security.model.UserEntity;

public interface UserRepo extends MongoRepository<UserEntity, String> {

    UserEntity findByUsername(String username);
}
