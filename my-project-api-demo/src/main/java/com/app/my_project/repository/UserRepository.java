package com.app.my_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.my_project.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsernameAndEmail(String username, String email);
    UserEntity findByUsernameAndPassword(String username, String password);
}
