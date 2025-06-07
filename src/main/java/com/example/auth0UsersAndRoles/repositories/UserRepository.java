package com.example.auth0UsersAndRoles.repositories;

import com.example.auth0UsersAndRoles.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends BaseRepository<User,Long> {



    @Query("SELECT u from User u where u.role.id != 2  " +
            "AND (:rol IS NULL OR u.role.description = :rol)" +
            " AND (:name IS NULL OR u.name like %:name%)")
    List<User> getEmpleoyees(@Param("rol") String rol,@Param("name")String name);

    @Query("SELECT u from User u where u.role.id = 2 AND (:name IS NULL OR u.name like %:name%)")
    List<User> getClients(@Param("name")String name);

}
