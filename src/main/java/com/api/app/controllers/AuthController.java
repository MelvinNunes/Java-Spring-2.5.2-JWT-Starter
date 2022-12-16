package com.api.app.controllers;


import com.api.app.dtos.UserCreateDTO;
import com.api.app.dtos.UserLoginDTO;
import com.api.app.models.User;
import com.api.app.security.UserServiceIMPL;
import com.api.app.services.RoleService;
import com.api.app.services.UserService;
import com.api.app.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final UserServiceIMPL userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final RoleService roleService;

    @PostMapping("/login")
    public ResponseEntity authenticate(@RequestBody @Valid UserLoginDTO request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        final UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        if (user != null){
            Map<String,String> response = new HashMap<>();
            response.put("access_token",jwtUtils.generateToken(user));
            return ResponseEntity.ok(response);
        }
        Map<String, String> error = new HashMap<>();
        error.put("message", "Check your Username/Password credentials!");
        return ResponseEntity.status(401).body(error);
    }

    @PostMapping("/logon/admin")
    public ResponseEntity createAdmin(@RequestBody @Valid UserCreateDTO userDTO){
        if (userService.userExists(userDTO.getUsername())){
            Map<String, String> error = new HashMap<>();
            error.put("message", "User "+userDTO.getUsername()+" already exists!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
        User user = new User();
        BeanUtils.copyProperties(userDTO, user) ;
        User created = userService.createUser(user);
        roleService.addRoleToUser(user.getUsername(), "ROLE_ADMIN");
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/logon/client")
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserCreateDTO userDTO){
        if (userService.userExists(userDTO.getUsername())){
            Map<String, String> error = new HashMap<>();
            error.put("Erro", "Já existe um usuário com esse username");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
        User user = new User();
        BeanUtils.copyProperties(userDTO, user) ;
        User created = userService.createUser(user);
        roleService.addRoleToUser(user.getUsername(), "ROLE_ADMIN");
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/refresh")
    public ResponseEntity refreshToken(HttpServletRequest request, Principal principal){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        Map<String, String> errorMessage = new HashMap<>();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring("Bearer ".length());
            UserDetails user = userDetailsService.loadUserByUsername(principal.getName());
            if (jwtUtils.isTokenValid(accessToken, user)){
                Map<String, String> refreshToken = new HashMap<>();
                refreshToken.put("refresh_token", jwtUtils.generateRefreshToken(user));
                return ResponseEntity.ok(refreshToken);
            }
            errorMessage.put("message","Actual Token isn´t valid");
            return ResponseEntity.status(401).body(errorMessage);
        }
        errorMessage.put("message","Authentication header wasn´t found.");
        return ResponseEntity.status(400).body(errorMessage);
    }

}
