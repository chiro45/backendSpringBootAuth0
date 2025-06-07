package com.example.auth0UsersAndRoles.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Roles extends Base {

    private String description;
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> usuarios = new HashSet<>();
}
