package com.website.skateshop.service;

import com.website.skateshop.model.UserModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserModel> findAllUsers();
    UserModel findUserById(int id);
    List<UserModel> findUsersByLogin(String login);
    UserModel addUser(UserModel user);
    UserModel updateUser(UserModel user);
    void deleteUser(int id);
    List<UserModel> findUsersPaginated(int page, int size);
    long countUsers();
}