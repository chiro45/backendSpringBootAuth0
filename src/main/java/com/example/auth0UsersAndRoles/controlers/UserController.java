package com.example.auth0UsersAndRoles.controlers;

import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;


import com.example.auth0UsersAndRoles.entities.dto.AssingRoleDTO;
import com.example.auth0UsersAndRoles.entities.dto.UserDTO;
import com.example.auth0UsersAndRoles.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/admin/users" ,produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;
    //traemos todos los usuarios
    @GetMapping
    public UsersPage getAllUsers() throws Exception {
        return userService.getAllUsers();
    }
    //traemos un usuariopor id
    @GetMapping("/getUserById")
    public User getUserById(@RequestBody UserDTO UserDTO) throws Exception {
        return userService.getUserById(UserDTO.getId());
    }
    //creamos el usuario
    @PostMapping("/createUser")
    public User createUser(@RequestBody UserDTO UserDTO) throws Exception {
        return userService.createUser(UserDTO);
    }
    //modificamos un usuario
    @PutMapping("/modifyUser")
    public User modifyUser(@RequestBody UserDTO UserDTO) throws Exception {
        return userService.modifyUser(UserDTO);
    }
    //eliminamos un usuario
    @DeleteMapping("/deleteUserById")
    public void deleteUser(@RequestBody UserDTO UserDTO) throws Exception {
        userService.deleteUser(UserDTO.getId());
    }
    //agregamos los roles a un usuario
    @PostMapping("/addRolesUser")
    public void assignRoles(@RequestBody AssingRoleDTO request) throws Exception {
        userService.assignRoles(request.getId(), request.getRoles());
    }
    //removemos roles a un usuario
    @PostMapping("/removeRolesUser")
    public void removeRoles(@RequestBody AssingRoleDTO request) throws Exception {
        userService.removeRoles(request.getId(), request.getRoles());
    }
}
