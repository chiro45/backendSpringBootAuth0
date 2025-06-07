package com.example.auth0UsersAndRoles.controlers;

import com.auth0.json.mgmt.Role;
import com.auth0.json.mgmt.RolesPage;

import com.example.auth0UsersAndRoles.entities.dto.RoleDTO;
import com.example.auth0UsersAndRoles.entities.dto.UserDTO;
import com.example.auth0UsersAndRoles.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/admin/roles", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public RolesPage getAllRoles() throws Exception {
        return roleService.getAllRoles();
    }

    @GetMapping("/getRoleById")
    public Role getRoleById(@RequestParam("id") String id) throws Exception {
        return  roleService.getRoleById(id);
    }

    @GetMapping("/getUserRoles")
    public RolesPage getUserRoles(@RequestBody UserDTO userDTO) throws Exception {
        return  roleService.getUserRoles(userDTO);
    }
    @GetMapping("/modifyRole")
    public Role modifyRole(@RequestBody RoleDTO roleDTO) throws Exception {
        return  roleService.modifyRole(roleDTO);
    }

    @PostMapping("/createRole")
    public Role createRole(@RequestBody RoleDTO roleDTO) throws Exception {
        return roleService.createRole(roleDTO);
    }

    @DeleteMapping("/deleteRole")
    public void deleteRole(@RequestParam("id") String id) throws Exception {
        roleService.deleteRole(id);
    }
}
