package com.website.skateshop.entity;

import com.website.skateshop.model.RoleModel;
import jakarta.persistence.*;

@Entity
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(columnNames = "charactertitle")
})
public class RoleEntity extends RoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public int getId() {
        return super.getId();
    }

    @Column(name = "charactertitle", nullable = false, length = 13, unique = true)
    @Override
    public String getCharacterTitle() {
        return super.getCharacterTitle();
    }

    public RoleEntity() {
        super();
    }

    public RoleEntity(int id, String characterTitle) {
        super(id, characterTitle);
    }
}