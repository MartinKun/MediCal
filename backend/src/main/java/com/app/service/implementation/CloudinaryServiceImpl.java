package com.app.service.implementation;

import com.app.config.CloudinaryConfig;
import com.app.service.FileService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryServiceImpl implements FileService {

    @Autowired
    CloudinaryConfig cloudinaryConfig;


    @Override
    public String uploadFile(MultipartFile file) {
        String key = UUID.randomUUID().toString();
        String path = "";
        Cloudinary cloudinary = cloudinaryConfig.cloudinary();
        Map params = ObjectUtils.asMap(
                "public_id", key,
                "overwrite", true,
                "resource_type", "image"
        );
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
            path = (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Error occurred while processing the file: %s", e.getMessage())
            );
        }
        return path;
    }

    @Override
    public void deleteFile(String key) {
        Cloudinary cloudinary = cloudinaryConfig.cloudinary();
        Map params = ObjectUtils.asMap("invalidate", true,
                "resource_type", "image");

        try {
            cloudinary.uploader().destroy(key, params);
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Error occurred while processing the file: %s", e.getMessage())
            );
        }
    }
}
