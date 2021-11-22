package com.example.testoauth2client.repository;

import com.example.testoauth2client.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JpaRepository가 들고 있음
// @Repository라는 어노테이션이 없어도 IoC되요, 이유는 JpaRepository를 상속했기 때문에... (빈으로 등록됨)
public interface UserRepository extends JpaRepository<User, Integer> {
    // findBy규칙 -> Username문법
    // SELECT * FROM user WHERE username = ?;
    public User findByUsername(String username);    // JPA Query method

    // JPA Query method 함수 구글링
//    // SELECT * FROM user WHERE email = ?
//    public User findByEmail();
}
