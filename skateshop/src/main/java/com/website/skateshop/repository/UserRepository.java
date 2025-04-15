package com.website.skateshop.repository;

import com.website.skateshop.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    List<UserEntity> findByLogin(String login);
    boolean existsByLogin(String login);
    boolean existsByPhoneNum(String phoneNum);
}