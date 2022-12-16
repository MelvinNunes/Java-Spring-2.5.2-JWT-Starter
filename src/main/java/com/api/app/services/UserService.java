package com.api.app.services;


import com.api.app.exceptions.ModelNotFound;
import com.api.app.models.User;
import com.api.app.repositories.UserRepo;
import com.api.app.utils.IMGUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;


    public User createUser(User user){
        user.setCreatedBy(user.getUsername());
        user.setUpdatedBy(user.getUsername());
        user.setFirstName(user.getFirstName().substring(0, 1).toUpperCase() + user.getFirstName().substring(1).toLowerCase());
        user.setLastName(user.getLastName().substring(0,1).toUpperCase() + user.getLastName().substring(1).toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User updateUser(User user){
        return userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getSpecificUser(Long id) {
        return userRepo.findById(id);
    }

    public User getByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(()-> new EntityNotFoundException("Não existe"));
    }

    public String getLoggedInUser(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Optional<User> getByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public String deleteUser(Long id){
        userRepo.deleteById(id);
        return "Deleted Successfully";
    }

    public Boolean userExists(String username){
        return userRepo.existsByUsername(username);
    }


    public User getMyById(Long id) throws ModelNotFound {
        User user = userRepo.findById(id).orElseThrow(()->new ModelNotFound("User com id "+id+" não foi encontrado!"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (user.getCreatedBy() == auth.getPrincipal()){
            return userRepo.findById(id).orElseThrow(()->new ModelNotFound("User com id "+id+" não foi encontrado!"));
        }
        return null;
    }

    public User getActualUser(){
        Optional<User> user = userRepo.findByUsername(getLoggedInUser());
        return user.get();
    }

    public User updateMyUser(User user) {
        Optional<User> userFound = userRepo.findByUsername(getLoggedInUser());
        if (userFound.isPresent()){
            return userRepo.save(user);
        }
        return null;
    }

    public String deleteMyUser() {
        Optional<User> user = userRepo.findByUsername(getLoggedInUser());
        if (user.isPresent()){
            userRepo.deleteByUsername(user.get().getUsername());
            return "Apagado com sucesso!";
        }
        return "DENIED! NOT OWNER!";
    }

    public Map<String, String> saveUserAvatar(MultipartFile avatar) throws IOException {
        Optional<User> user = userRepo.findByUsername(getLoggedInUser());
        if (user.isPresent()){
            user.get().setAvatar(IMGUtils.compressImage(avatar.getBytes()));
            userRepo.save(user.get());
            Map<String, String> message = new HashMap<>();
            message.put("message", "Avatar updated successfully");
            return message;
        }
        return null;
    }

    public byte[] getUserAvatar() {
        Optional<User> user = userRepo.findByUsername(getLoggedInUser());
        if (user.isPresent()){
            byte[] avatar = IMGUtils.decompressImage(user.get().getAvatar());
            return avatar;
        }
        return null;
    }

}
