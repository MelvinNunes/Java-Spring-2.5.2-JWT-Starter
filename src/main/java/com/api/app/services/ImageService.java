package com.api.app.services;

import com.api.app.models.Image;
import com.api.app.repositories.ImageRepo;
import com.api.app.utils.IMGUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {
    @Autowired
    private ImageRepo imageRepo;

    public Image saveImage(MultipartFile image) throws IOException {
        return imageRepo.save(Image.builder()
                .name(image.getOriginalFilename())
                .img(IMGUtils.compressImage(image.getBytes())).build());
    }

    public Image downloadImage(Long id){
        Image img = imageRepo.findById(id).orElseThrow();
        img.setImg(IMGUtils.decompressImage(img.getImg()));
        return img;
    }
}
