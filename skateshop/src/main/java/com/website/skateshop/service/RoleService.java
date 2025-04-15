package com.website.skateshop.service;

import com.website.skateshop.model.RoleModel;

import java.util.List;

public interface RoleService {
    List<RoleModel> findAllRoles();
    RoleModel findRoleById(int id);
    List<RoleModel> findRolesByTitle(String title);
    RoleModel addRole(RoleModel role);
    RoleModel updateRole(RoleModel role);
    void deleteRole(int id);

    List<RoleModel> findRolesPaginated(int page, int size);
    long countRoles();
}