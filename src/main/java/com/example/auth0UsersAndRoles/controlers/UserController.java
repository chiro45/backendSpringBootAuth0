package com.example.auth0UsersAndRoles.controlers;


import com.auth0.json.mgmt.users.UsersPage;


import com.example.auth0UsersAndRoles.entities.Roles;
import com.example.auth0UsersAndRoles.entities.User;
import com.example.auth0UsersAndRoles.entities.dto.AssingRoleDTO;
import com.example.auth0UsersAndRoles.entities.dto.UserDTO;
import com.example.auth0UsersAndRoles.repositories.RoleRepository;
import com.example.auth0UsersAndRoles.services.UserBBDDService;
import com.example.auth0UsersAndRoles.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path="/api/admin/users" ,produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private UserService userService;
    private UserBBDDService userBBDDService;
    private RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository,UserBBDDService userBBDDService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userBBDDService = userBBDDService;
    }

    //traemos todos los usuarios
    @GetMapping
    public List<User> getAllUsers() throws Exception {
        return userBBDDService.findAll();
    }
    //traemos un usuariopor id
    @GetMapping("/getUserById")
    public User getUserById(@RequestBody UserDTO UserDTO) throws Exception {
        return userBBDDService.findById(UserDTO.getAuth0Id());
    }

    //creamos el usuario
    @PostMapping("/createUser")
    public User createUser(@RequestBody UserDTO UserDTO) throws Exception {
        com.auth0.json.mgmt.users.User newUser = userService.createUser(UserDTO);
        userService.assignRoles(newUser.getId(), UserDTO.getRoles());
        Set<Roles> rolesAsignados = UserDTO.getRoles().stream()
                .map(idRol -> roleRepository.findByAuth0RoleId(idRol)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + idRol)))
                .collect(Collectors.toSet());

        User userbbdd = User.builder()
                .auth0Id(newUser.getId())
                .name(newUser.getName())
                .roles(rolesAsignados)
                .nickName(UserDTO.getNickName())
                .userEmail(newUser.getEmail())
                .build();
        return userBBDDService.save(userbbdd) ;
    }

    //modificamos un usuario
    @PutMapping("/modifyUser")
    public User modifyUser(@RequestBody UserDTO UserDTO) throws Exception {
        com.auth0.json.mgmt.users.User newUser = userService.modifyUser(UserDTO);

        Set<Roles> rolesAsignados = UserDTO.getRoles().stream()
                .map(idRol -> roleRepository.findByAuth0RoleId(idRol)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + idRol)))
                .collect(Collectors.toSet());

        User updatedUser = User.builder()
                .auth0Id(newUser.getId())
                .name(newUser.getName())
                .userEmail(newUser.getEmail())
                .nickName(UserDTO.getNickName())
                .roles(rolesAsignados)
                .lastName(UserDTO.getLastName())
                .build();
        updatedUser.setId(UserDTO.getId());

        return userBBDDService.update(updatedUser);
    }

    //eliminamos un usuario
    @DeleteMapping("/deleteUserById")
    public void deleteUser(@RequestBody UserDTO UserDTO) throws Exception {
        userBBDDService.delete(UserDTO.getAuth0Id());
    }

    //eliminamos un usuario
    @DeleteMapping("/deleteUserByIdFisic")
    public void deleteUserFisic(@RequestBody UserDTO UserDTO) throws Exception {
        userBBDDService.deleteFisic(UserDTO.getAuth0Id());
        userService.deleteUser(UserDTO.getAuth0Id());
    }

    //agregamos los roles a un usuario
    @PostMapping("/addRolesUser")
    public User assignRoles(@RequestBody AssingRoleDTO request) throws Exception {
        userService.assignRoles(request.getId(), request.getRoles());
        User user = userBBDDService.findById(request.getId());
        Set<Roles> rolesAAgregar = request.getRoles().stream()
                .map(idRol -> roleRepository.findByAuth0RoleId(idRol)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + idRol)))
                .collect(Collectors.toSet());
        // Agregar a los roles actuales
        user.getRoles().addAll(rolesAAgregar);

        return userBBDDService.update(user);
    }

    //removemos roles a un usuario
    @PostMapping("/removeRolesUser")
    public User removeRoles(@RequestBody AssingRoleDTO request) throws Exception {
        userService.removeRoles(request.getId(), request.getRoles());
        User user = userBBDDService.findById(request.getId());

        Set<Roles> rolesAEliminar = request.getRoles().stream()
                .map(idRol -> roleRepository.findByAuth0RoleId(idRol)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + idRol)))
                .collect(Collectors.toSet());

        user.getRoles().removeAll(rolesAEliminar);
        return userBBDDService.update(user);
    }
}
