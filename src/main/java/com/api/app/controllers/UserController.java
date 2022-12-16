package com.api.app.controllers;

import com.api.app.dtos.UserUpdateDTO;
import com.api.app.exceptions.ModelNotFound;
import com.api.app.models.User;
import com.api.app.services.RoleService;
import com.api.app.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/me")
    public ResponseEntity getMyUser(){
        return ResponseEntity.ok(userService.getActualUser());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateUser (@PathVariable Long id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) throws ModelNotFound {
        Optional<User> userOptional = userService.getSpecificUser(id);
        if (!userOptional.isPresent()){
            Map<String, String> error = new HashMap<>();
            error.put("Erro", "Não existe nenhum usuário com esse id!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateDTO, user);
        user.setId(userOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateMyUser(user));
    }

    @GetMapping("/me/avatar")
    public ResponseEntity getMyAvatar() {
        byte [] avatar = userService.getUserAvatar();
        if (avatar != null){
            return ResponseEntity.status(200).contentType(MediaType.IMAGE_PNG).body(avatar);
        }
        Map<String, String> info = new HashMap<>();
        info.put("message", "User doesnt have avatar");
        return ResponseEntity.status(200).body(info);
    }

    @PutMapping("/me/avatar")
    public ResponseEntity updateMyAvatar(@RequestParam("avatar") MultipartFile avatar) throws IOException {
        return ResponseEntity.status(200).contentType(MediaType.IMAGE_PNG).body(userService.saveUserAvatar(avatar));
    }

}
