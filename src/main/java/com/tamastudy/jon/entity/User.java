package com.tamastudy.jon.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class User extends JpaBaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private String email;
    private String role;

    private String provider; // google, etc...
    private String providerId; // sub id
}
