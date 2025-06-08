package com.example.auth0UsersAndRoles.controlers;

import com.auth0.json.mgmt.Role;
import com.auth0.json.mgmt.RolesPage;

import com.example.auth0UsersAndRoles.entities.Roles;
import com.example.auth0UsersAndRoles.entities.dto.RoleDTO;
import com.example.auth0UsersAndRoles.entities.dto.UserDTO;
import com.example.auth0UsersAndRoles.services.RoleAuth0Service;
import com.example.auth0UsersAndRoles.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/admin/roles", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoleController {

    private RoleAuth0Service roleService;

    private RoleService roleServicebbdd;

    public RoleController(RoleService roleServicebbdd, RoleAuth0Service roleService) {
        this.roleServicebbdd = roleServicebbdd;
        this.roleService = roleService;
    }


    @GetMapping
    public List<Roles> getAllRoles() throws Exception {
        return roleServicebbdd.findAll();
    }

    @GetMapping("/getRoleById")
    public Roles getRoleById(@RequestParam("id") String id) throws Exception {
        return  roleServicebbdd.findById(id);
    }

    @PutMapping("/modifyRole")
    public Roles modifyRole(@RequestBody RoleDTO roleDTO) throws Exception {
        Role updatedRoleAuth0 = roleService.modifyRole(roleDTO);
        Roles updatedRole = Roles.builder()
                .name(updatedRoleAuth0.getName())
                .description(updatedRoleAuth0.getDescription())
                .auth0RoleId(updatedRoleAuth0.getId())
                .build();
        updatedRole.setId(roleDTO.getId());
        return roleServicebbdd.update(updatedRole) ;
    }

    @PostMapping("/createRole")
    public Roles createRole(@RequestBody RoleDTO roleDTO) throws Exception {
        Role rolAuth = roleService.createRole(roleDTO);
        Roles roles = Roles.builder()
                .auth0RoleId(rolAuth.getId())
                .description(roleDTO.getDescription())
                .name(roleDTO.getName())
                .build();
        return roleServicebbdd.save(roles);
    }

    @DeleteMapping("/deleteRole")
    public void deleteRole(@RequestParam("id") String id) throws Exception {
        roleServicebbdd.delete(id);
    }

    @DeleteMapping("/deleteRoleFisic")
    public void deleteRoleFisic(@RequestParam("id") String id) throws Exception {
        roleServicebbdd.deleteFisic(id);
        roleService.deleteRole(id);
    }


}
