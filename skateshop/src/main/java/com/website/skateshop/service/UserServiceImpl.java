package com.website.skateshop.service;

import com.website.skateshop.entity.RoleEntity;
import com.website.skateshop.entity.UserEntity;
import com.website.skateshop.model.RoleModel;
import com.website.skateshop.model.UserModel;
import com.website.skateshop.repository.RoleRepository;
import com.website.skateshop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
            throw new IllegalArgumentException("Логин уже существует");
        }
        if (userRepository.existsByPhoneNum(user.getPhoneNum())) {
            throw new IllegalArgumentException("Номер телефона уже используется");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            RoleEntity defaultRole = roleRepository.findByCharacterTitle("ROLE_USER")
                    .orElseThrow(() -> new IllegalStateException("Default role not found"));
            user.setRole(new RoleModel(defaultRole.getId(), defaultRole.getCharacterTitle()));
        }

        return convertToModel(userRepository.save(convertToEntity(user)));
    }

    private void authenticateUser(String username, String rawPassword) {
        UserDetails userDetails = loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                rawPassword,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Attempting to authenticate user: " + username);
        List<UserEntity> users = userRepository.findByLogin(username);

        if (users.isEmpty()) {
            System.out.println("User not found: " + username);
            throw new UsernameNotFoundException("User not found");
        }

        UserEntity user = users.get(0);
        System.out.println("Found user: " + user.getLogin());
        System.out.println("Stored password hash: " + user.getPassword());

        return User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .authorities(mapRolesToAuthorities(user.getRole()))
                .build();
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(RoleEntity role) {
        if (role == null) {
            return Collections.emptyList();
        }
        String roleName = role.getCharacterTitle().startsWith("ROLE_")
                ? role.getCharacterTitle()
                : "ROLE_" + role.getCharacterTitle();
        return List.of(new SimpleGrantedAuthority(roleName));
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