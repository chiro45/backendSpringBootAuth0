package com.example.auth0UsersAndRoles.entities.fixedEntities;

import com.example.auth0UsersAndRoles.entities.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="role")
@Data //Getters and Setters
@NoArgsConstructor
@AllArgsConstructor
public class Role extends Base {
    private String description;

    @Column(name = "auth0_role_id")
    private String auth0RoleId;
}
