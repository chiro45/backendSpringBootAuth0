package com.example.auth0UsersAndRoles.entities;

import com.example.auth0UsersAndRoles.entities.fixedEntities.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User extends Base {

    private String userEmail;
    private String password;

    @OneToOne
    private Role role;

    private String name;
    private String lastName;

    @Column(name = "auth0_id")
    private String auth0Id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("usuarios")
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Roles> roles = new HashSet<>();
}
