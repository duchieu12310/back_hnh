package com.thatannhien.controller.general;

import com.thatannhien.constant.AppConstants;
import com.thatannhien.dto.CollectionWrapper;
import com.thatannhien.dto.general.ImageResponse;
import com.thatannhien.dto.general.UploadedImageResponse;
import com.thatannhien.mapper.general.ImageMapper;
import com.thatannhien.repository.general.ImageRepository;
import com.thatannhien.service.general.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/images")
@AllArgsConstructor
@CrossOrigin(AppConstants.FRONTEND_HOST)
@Slf4j
public class ImageController {

    private ImageService imageService;
    private ImageRepository imageRepository;
    private ImageMapper imageMapper;

    @PostMapping("/upload-single")
    public ResponseEntity<UploadedImageResponse> uploadSingleImage(@RequestParam("image") MultipartFile image) {
        return ResponseEntity.status(HttpStatus.OK).body(imageService.store(image));
    }

    @PostMapping("/upload-multiple")
    public ResponseEntity<CollectionWrapper<UploadedImageResponse>> uploadMultipleImages(@RequestParam("images") MultipartFile[] images) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CollectionWrapper<>(Stream.of(images)
                        .map(imageService::store)
                        .collect(Collectors.toList())));
    }

    @GetMapping("/{imageName:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String imageName, HttpServletRequest request) {
        Resource resource = imageService.load(imageName);

        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{imageName:.+}")
    public ResponseEntity<Void> deleteImage(@PathVariable String imageName) {
        imageService.delete(imageName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMultipleImages(@RequestBody List<String> imageNames) {
        imageNames.forEach(imageService::delete);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Update path của image theo ID
     * Body: { "path": "https://thegioitradao.com/wp-content/uploads/2025/11/image.jpg" }
     */
    @PutMapping("/{id}/path")
    public ResponseEntity<ImageResponse> updateImagePath(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newPath = request.get("path");
        if (newPath == null || newPath.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
        return imageRepository.findById(id)
                .map(image -> {
                    image.setPath(newPath.trim());
                    return imageRepository.save(image);
                })
                .map(imageMapper::entityToResponse)
                .map(response -> ResponseEntity.status(HttpStatus.OK).body(response))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Update path của nhiều images cùng lúc
     * Body: { "updates": [{ "id": 1, "path": "https://..." }, { "id": 2, "path": "https://..." }] }
     */
    @PutMapping("/batch-update-paths")
    public ResponseEntity<CollectionWrapper<ImageResponse>> batchUpdateImagePaths(@RequestBody Map<String, List<Map<String, Object>>> request) {
        List<Map<String, Object>> updates = request.get("updates");
        if (updates == null || updates.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<ImageResponse> updatedImages = updates.stream()
                .map(update -> {
                    Long id = Long.valueOf(update.get("id").toString());
                    String newPath = update.get("path").toString();
                    return imageRepository.findById(id)
                            .map(image -> {
                                image.setPath(newPath.trim());
                                return imageRepository.save(image);
                            })
                            .map(imageMapper::entityToResponse)
                            .orElse(null);
                })
                .filter(response -> response != null)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CollectionWrapper<>(updatedImages));
    }

}
