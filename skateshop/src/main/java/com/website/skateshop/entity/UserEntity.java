package com.website.skateshop.entity;

import com.website.skateshop.model.UserModel;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "client")
public class UserEntity extends UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public int getId() {
        return super.getId();
    }

    @Column(name = "firstname", nullable = false, length = 30)
    @Override
    public String getName() {
        return super.getName();
    }

    @Column(name = "surname", nullable = false, length = 30)
    @Override
    public String getSurname() {
        return super.getSurname();
    }

    @Column(name = "lastname", length = 30)
    @Override
    public String getLastName() {
        return super.getLastName();
    }

    @Column(name = "phonenumber", nullable = false, unique = true, length = 20)
    @Override
    public String getPhoneNum() {
        return super.getPhoneNum();
    }

    @Column(name = "clientlogin", nullable = false, unique = true, length = 20)
    @Override
    public String getLogin() {
        return super.getLogin();
    }

    @Column(name = "clientpassword", nullable = false)
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    public UserEntity() {
        super(0, null, null, null, null, null, null);
    }

    public UserEntity(int id, String name, String surname, String lastName,
                      String phoneNum, String login, String password) {
        super(id, name, surname, lastName, phoneNum, login, password);
    }
}