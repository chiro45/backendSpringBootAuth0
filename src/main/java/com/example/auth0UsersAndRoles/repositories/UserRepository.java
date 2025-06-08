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

    User getUserByAuth0Id (String id);

}
