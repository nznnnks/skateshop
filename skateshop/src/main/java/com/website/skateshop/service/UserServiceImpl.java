package com.website.skateshop.service;

import com.website.skateshop.entity.RoleEntity;
import com.website.skateshop.entity.UserEntity;
import com.website.skateshop.model.RoleModel;
import com.website.skateshop.model.UserModel;
import com.website.skateshop.repository.RoleRepository;
import com.website.skateshop.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
        RoleModel roleModel = null;
        if (entity.getRole() != null) {
            roleModel = new RoleModel(entity.getRole().getId(), entity.getRole().getCharacterTitle());
        }

        return new UserModel(
                entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getLastName(),
                entity.getPhoneNum(),
                entity.getLogin(),
                entity.getPassword(),
                roleModel
        );
    }

    private UserEntity convertToEntity(UserModel model) {
        UserEntity entity = new UserEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setSurname(model.getSurname());
        entity.setLastName(model.getLastName());
        entity.setPhoneNum(model.getPhoneNum());
        entity.setLogin(model.getLogin());
        entity.setPassword(model.getPassword());

        if (model.getRole() != null && model.getRole().getId() != 0) {
            RoleEntity role = roleRepository.findById(model.getRole().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Role not found"));
            entity.setRole(role);
        }

        return entity;
    }
}