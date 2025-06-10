package com.example.auth0UsersAndRoles.controlers;


import com.example.auth0UsersAndRoles.entities.Roles;
import com.example.auth0UsersAndRoles.entities.User;
import com.example.auth0UsersAndRoles.entities.dto.AssingRoleDTO;
import com.example.auth0UsersAndRoles.entities.dto.UserDTO;
import com.example.auth0UsersAndRoles.repositories.RoleRepository;
import com.example.auth0UsersAndRoles.services.UserBBDDService;
import com.example.auth0UsersAndRoles.services.UserAuth0Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@RestController
@RequestMapping(path = "/api/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserAuth0Service userAuth0Service;
    private final UserBBDDService userBBDDService;
    private final RoleRepository roleRepository;

    public UserController(UserAuth0Service userAuth0Service, RoleRepository roleRepository, UserBBDDService userBBDDService) {
        this.userAuth0Service = userAuth0Service;
        this.roleRepository = roleRepository;
        this.userBBDDService = userBBDDService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userBBDDService.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener los usuarios: " + e.getMessage());
        }
    }

    @PostMapping("/getUserById")
    public ResponseEntity<?> getUserById(@RequestBody UserDTO userDTO) {
        try {
            User user = userBBDDService.findById(userDTO.getAuth0Id());
            if(user == null) {
                return ResponseEntity.ok(false);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado: " + e.getMessage());
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            com.auth0.json.mgmt.users.User newUser = userAuth0Service.createUser(userDTO);
            userAuth0Service.assignRoles(newUser.getId(), userDTO.getRoles());

            Set<Roles> rolesAsignados = userDTO.getRoles().stream()
                    .map(idRol -> roleRepository.findByAuth0RoleId(idRol)
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + idRol)))
                    .collect(Collectors.toSet());

            User userBBDD = User.builder()
                    .auth0Id(newUser.getId())
                    .name(newUser.getName())
                    .roles(rolesAsignados)
                    .nickName(userDTO.getNickName())
                    .userEmail(newUser.getEmail())
                    .build();

            return ResponseEntity.ok(userBBDDService.save(userBBDD));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al crear el usuario: " + e.getMessage());
        }
    }

    @PostMapping("/createUserClient")
    public ResponseEntity<?> createUserClient(@RequestBody UserDTO userDTO) {
        try {
            com.auth0.json.mgmt.users.User userAuth0 = userAuth0Service.getUserById(userDTO.getAuth0Id());
            if(userAuth0 == null) {
                return ResponseEntity.internalServerError().body("El usuario no existe");
            }

            userAuth0Service.assignRoles(userAuth0.getId(), userDTO.getRoles());

            Set<Roles> rolesAsignados = userDTO.getRoles().stream()
                    .map(idRol -> roleRepository.findByAuth0RoleId(idRol)
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + idRol)))
                    .collect(Collectors.toSet());

            User userBBDD = User.builder()
                    .auth0Id(userAuth0.getId())
                    .name(userAuth0.getName())
                    .roles(rolesAsignados)
                    .nickName(userDTO.getNickName())
                    .userEmail(userAuth0.getEmail())
                    .build();

            return ResponseEntity.ok(userBBDDService.save(userBBDD));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al crear cliente: " + e.getMessage());
        }
    }

    @PutMapping("/modifyUser")
    public ResponseEntity<?> modifyUser(@RequestBody UserDTO userDTO) {
        try {
            com.auth0.json.mgmt.users.User newUser = userAuth0Service.modifyUser(userDTO);

            Set<Roles> rolesAsignados = userDTO.getRoles().stream()
                    .map(idRol -> roleRepository.findByAuth0RoleId(idRol)
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + idRol)))
                    .collect(Collectors.toSet());

            User updatedUser = User.builder()
                    .auth0Id(newUser.getId())
                    .name(newUser.getName())
                    .userEmail(newUser.getEmail())
                    .nickName(userDTO.getNickName())
                    .roles(rolesAsignados)
                    .lastName(userDTO.getLastName())
                    .build();
            updatedUser.setId(userDTO.getId());

            return ResponseEntity.ok(userBBDDService.update(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al modificar usuario: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteUserById")
    public ResponseEntity<?> deleteUser(@RequestBody UserDTO userDTO) {
        try {
            userBBDDService.delete(userDTO.getAuth0Id());
            return ResponseEntity.ok("Usuario eliminado (lógico) correctamente.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al eliminar usuario: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteUserByIdFisic")
    public ResponseEntity<?> deleteUserFisic(@RequestBody UserDTO userDTO) {
        try {
            userBBDDService.deleteFisic(userDTO.getAuth0Id());
            userAuth0Service.deleteUser(userDTO.getAuth0Id());
            return ResponseEntity.ok("Usuario eliminado físicamente.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al eliminar físicamente al usuario: " + e.getMessage());
        }
    }

    @PostMapping("/addRolesUser")
    public ResponseEntity<?> assignRoles(@RequestBody AssingRoleDTO request) {
        try {
            userAuth0Service.assignRoles(request.getId(), request.getRoles());
            User user = userBBDDService.findById(request.getId());

            Set<Roles> rolesAAgregar = request.getRoles().stream()
                    .map(idRol -> roleRepository.findByAuth0RoleId(idRol)
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + idRol)))
                    .collect(Collectors.toSet());

            user.getRoles().addAll(rolesAAgregar);
            return ResponseEntity.ok(userBBDDService.update(user));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al agregar roles: " + e.getMessage());
        }
    }

    @PostMapping("/removeRolesUser")
    public ResponseEntity<?> removeRoles(@RequestBody AssingRoleDTO request) {
        try {
            userAuth0Service.removeRoles(request.getId(), request.getRoles());
            User user = userBBDDService.findById(request.getId());

            Set<Roles> rolesAEliminar = request.getRoles().stream()
                    .map(idRol -> roleRepository.findByAuth0RoleId(idRol)
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + idRol)))
                    .collect(Collectors.toSet());

            user.getRoles().removeAll(rolesAEliminar);
            return ResponseEntity.ok(userBBDDService.update(user));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al quitar roles: " + e.getMessage());
        }
    }
}
