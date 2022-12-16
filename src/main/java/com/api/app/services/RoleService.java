package com.api.app.services;



import com.api.app.models.Role;
import com.api.app.models.User;
import com.api.app.repositories.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepo roleRepo;
    private final UserService userService;


    public Role createRole (Role role){
        return roleRepo.save(role);
    }

    public List<Role> getAllRoles(){
        return roleRepo.findAll();
    }

    public Optional<Role> getRoleByName(String name){
        return roleRepo.findByName(name);
    }

    public Optional<User> addRoleToUser(String username, String rolename){
        User user = userService.getByUsername(username);
        Optional<Role> role = getRoleByName(rolename);
        user.getRoles().add(role.get());
        return Optional.ofNullable(userService.updateUser(user));
    }

    public Role updateRole(Role item){
        return roleRepo.save(item);
    }

    public Optional<Role> getSpecificRole(Long id) {
        return roleRepo.findById(id);
    }

    public String deleteRole(Long id){
        roleRepo.deleteById(id);
        return "Deleted Successfully";
    }
}
