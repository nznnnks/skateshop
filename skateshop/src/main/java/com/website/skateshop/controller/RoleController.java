package com.website.skateshop.controller;

import com.website.skateshop.model.RoleModel;
import com.website.skateshop.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String getAllRoles(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("roles", roleService.findRolesPaginated(page, size));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) roleService.countRoles() / size));
        return "roleList";
    }

    @GetMapping("/searchById")
    public String findRoleById(@RequestParam int id, Model model) {
        RoleModel role = roleService.findRoleById(id);
        model.addAttribute("roles", role != null ? List.of(role) : List.of());
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        return "roleList";
    }

    @GetMapping("/searchByTitle")
    public String findRolesByTitle(@RequestParam String title, Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        List<RoleModel> roles = roleService.findRolesByTitle(title);
        model.addAttribute("roles", roles);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) roles.size() / size));
        return "roleList";
    }

    @PostMapping("/add")
    public String addRole(@RequestParam String characterTitle) {
        RoleModel newRole = new RoleModel(0, characterTitle);
        roleService.addRole(newRole);
        return "redirect:/roles";
    }

    @PostMapping("/update")
    public String updateRole(@RequestParam int id, @RequestParam String characterTitle) {
        RoleModel updatedRole = new RoleModel(id, characterTitle);
        roleService.updateRole(updatedRole);
        return "redirect:/roles";
    }

    @PostMapping("/delete")
    public String deleteRole(@RequestParam int id) {
        roleService.deleteRole(id);
        return "redirect:/roles";
    }
}