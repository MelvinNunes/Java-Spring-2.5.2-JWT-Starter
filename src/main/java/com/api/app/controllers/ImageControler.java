package com.api.app.controllers;

import com.api.app.models.Image;
import com.api.app.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/image")
public class ImageControler {
    @Autowired
    private ImageService imageService;

    @PostMapping("/")
    public ResponseEntity save(@RequestParam("image") MultipartFile file) throws IOException {
        return ResponseEntity.ok(imageService.saveImage(file));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id){
        Image img = imageService.downloadImage(id);
        return ResponseEntity.status(200).contentType(MediaType.IMAGE_PNG).body(img.getImg());
    }

}
