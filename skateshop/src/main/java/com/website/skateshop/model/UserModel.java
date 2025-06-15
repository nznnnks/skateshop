package com.website.skateshop.model;

import jakarta.validation.constraints.*;

public class UserModel {
    private Integer id;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(max = 30, message = "Имя не может быть длиннее 30 символов")
    private String name;

    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(max = 30, message = "Фамилия не может быть длиннее 30 символов")
    private String surname;

    @Size(max = 30, message = "Отчество не может быть длиннее 30 символов")
    private String lastName;

    @NotBlank(message = "Номер телефона не может быть пустым")
    @Size(max = 20, message = "Номер телефона не может быть длиннее 20 символов")
    private String phoneNum;

    @NotBlank(message = "Логин не может быть пустым")
    @Size(max = 20, message = "Логин не может быть длиннее 20 символов")
    private String login;

    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    private RoleModel role;

    public UserModel() {
    }

    public UserModel(Integer id, String name, String surname, String lastName,
                     String phoneNum, String login, String password, RoleModel role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.lastName = lastName;
        this.phoneNum = phoneNum;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleModel getRole() {
        return role;
    }

    public void setRole(RoleModel role) {
        this.role = role;
    }
}