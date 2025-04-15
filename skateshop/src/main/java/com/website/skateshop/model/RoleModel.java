package com.website.skateshop.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RoleModel {
    private int id;

    @NotBlank(message = "Название роли не может быть пустым")
    @Size(max = 13, message = "Название роли не может быть длиннее 13 символов")
    private String characterTitle;

    public RoleModel() {
    }

    public RoleModel(int id, String characterTitle) {
        this.id = id;
        this.characterTitle = characterTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCharacterTitle() {
        return characterTitle;
    }

    public void setCharacterTitle(String characterTitle) {
        this.characterTitle = characterTitle;
    }
}