package com.website.skateshop.service;

import com.website.skateshop.entity.UserEntity;
import com.website.skateshop.model.UserModel;
import com.website.skateshop.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserModel> findAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public UserModel findUserById(int id) {
        return userRepository.findById(id)
                .map(this::convertToModel)
                .orElse(null);
    }

    @Override
    public List<UserModel> findUsersByLogin(String login) {
        return userRepository.findByLogin(login).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public UserModel addUser(UserModel user) {
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new IllegalArgumentException("Login already exists");
        }
        if (userRepository.existsByPhoneNum(user.getPhoneNum())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        UserEntity entity = convertToEntity(user);
        UserEntity saved = userRepository.save(entity);
        return convertToModel(saved);
    }

    @Override
    public UserModel updateUser(UserModel user) {
        UserEntity entity = convertToEntity(user);
        UserEntity updated = userRepository.save(entity);
        return convertToModel(updated);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserModel> findUsersPaginated(int page, int size) {
        Page<UserEntity> result = userRepository.findAll(PageRequest.of(page, size));
        return result.getContent().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public long countUsers() {
        return userRepository.count();
    }

    private UserModel convertToModel(UserEntity entity) {
        return new UserModel(
                entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getLastName(),
                entity.getPhoneNum(),
                entity.getLogin(),
                entity.getPassword()
        );
    }

    private UserEntity convertToEntity(UserModel model) {
        return new UserEntity(
                model.getId(),
                model.getName(),
                model.getSurname(),
                model.getLastName(),
                model.getPhoneNum(),
                model.getLogin(),
                model.getPassword()
        );
    }
}