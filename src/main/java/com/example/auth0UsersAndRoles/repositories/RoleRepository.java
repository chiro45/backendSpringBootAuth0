package com.example.auth0UsersAndRoles.repositories;

import com.example.auth0UsersAndRoles.entities.fixedEntities.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends BaseRepository<Role,Long> {

}
