package com.website.skateshop.controller;

import com.website.skateshop.model.UserModel;
import com.website.skateshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String getAllUsers(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("users", userService.findUsersPaginated(page, size));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) userService.countUsers() / size));
        return "userList";
    }

    @GetMapping("/searchById")
    public String findUserById(@RequestParam int id, Model model) {
        UserModel user = userService.findUserById(id);
        model.addAttribute("users", user != null ? List.of(user) : List.of());
        model.addAttribute("totalPages", 1);
        model.addAttribute("currentPage", 0);
        return "userList";
    }

    @GetMapping("/searchByLogin")
    public String findUsersByLogin(@RequestParam String login, Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        List<UserModel> users = userService.findUsersByLogin(login);
        model.addAttribute("users", users);
        model.addAttribute("totalPages", (int) Math.ceil((double) users.size() / size));
        model.addAttribute("currentPage", page);
        return "userList";
    }

    @PostMapping("/add")
    public String addUser(@RequestParam String name,
                          @RequestParam String surname,
                          @RequestParam String lastName,
                          @RequestParam String phoneNum,
                          @RequestParam String login,
                          @RequestParam String password) {
        UserModel newUser = new UserModel();
        // Не устанавливаем ID!
        newUser.setName(name);
        newUser.setSurname(surname);
        newUser.setLastName(lastName);
        newUser.setPhoneNum(phoneNum);
        newUser.setLogin(login);
        newUser.setPassword(password);

        userService.addUser(newUser);
        return "redirect:/users";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam int id,
                             @RequestParam String name,
                             @RequestParam String surname,
                             @RequestParam String lastName,
                             @RequestParam String phoneNum,
                             @RequestParam String login,
                             @RequestParam String password) {
        UserModel updatedUser = new UserModel(id, name, surname, lastName, phoneNum, login, password);
        userService.updateUser(updatedUser);
        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam int id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}