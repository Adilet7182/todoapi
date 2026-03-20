package com.example.todoapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    //найти пользователя по id
    Optional<User> findByTelegramId(Long telegramId);

}
