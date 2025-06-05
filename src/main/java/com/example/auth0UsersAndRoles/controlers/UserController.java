package com.example.auth0UsersAndRoles.controlers;

import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;


import com.example.auth0UsersAndRoles.entities.dto.AssingRoleDTO;
import com.example.auth0UsersAndRoles.entities.dto.UserDTO;
import com.example.auth0UsersAndRoles.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
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
    public User getUserById(@RequestBody UserDTO userDTO) throws Exception {
        return userService.getUserById(userDTO.getId());
    }
    //creamos el usuario
    @PostMapping("/createUser")
    public User createUser(@RequestBody UserDTO userDTO) throws Exception {
        return userService.createUser(userDTO);
    }
    //modificamos un usuario
    @PutMapping("/modifyUser")
    public User modifyUser(@RequestBody UserDTO userDto) throws Exception {
        return userService.modifyUser(userDto);
    }
    //eliminamos un usuario
    @DeleteMapping("/deleteUserById")
    public void deleteUser(@RequestBody UserDTO userDto) throws Exception {
        userService.deleteUser(userDto.getId());
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
