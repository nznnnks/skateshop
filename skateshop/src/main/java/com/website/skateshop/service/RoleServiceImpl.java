package com.website.skateshop.service;

import com.website.skateshop.entity.RoleEntity;
import com.website.skateshop.model.RoleModel;
import com.website.skateshop.repository.RoleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleModel> findAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public RoleModel findRoleById(int id) {
        return roleRepository.findById(id)
                .map(this::convertToModel)
                .orElse(null);
    }

    @Override
    public List<RoleModel> findRolesByTitle(String title) {
        return roleRepository.findByCharacterTitle(title)
                .stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public RoleModel addRole(RoleModel role) {
        RoleEntity entity = convertToEntity(role);
        RoleEntity saved = roleRepository.save(entity);
        return convertToModel(saved);
    }

    @Override
    public RoleModel updateRole(RoleModel role) {
        RoleEntity entity = convertToEntity(role);
        RoleEntity updated = roleRepository.save(entity);
        return convertToModel(updated);
    }

    @Override
    public void deleteRole(int id) {
        roleRepository.deleteById(id);
    }

    @Override
    public List<RoleModel> findRolesPaginated(int page, int size) {
        return roleRepository.findAll(PageRequest.of(page, size)).getContent().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public long countRoles() {
        return roleRepository.count();
    }

    private RoleModel convertToModel(RoleEntity entity) {
        return new RoleModel(
                entity.getId(),
                entity.getCharacterTitle()
        );
    }

    private RoleEntity convertToEntity(RoleModel model) {
        return new RoleEntity(
                model.getId(),
                model.getCharacterTitle()
        );
    }
}