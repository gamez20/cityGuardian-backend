package co.edu.uniquindio.cityguardian.controller;

import co.edu.uniquindio.cityguardian.dto.ImagenDTO;
import co.edu.uniquindio.cityguardian.services.ImagenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/imagenes")
public class ImagenController {

    private final ImagenService imagenService;

    public ImagenController(ImagenService imagenService) {
        this.imagenService = imagenService;
    }

    @PostMapping("/subir")
    public ResponseEntity<List<ImagenDTO>> subirImagenes(@RequestPart("imagenes") List<MultipartFile> imagenes) {
        try {
            return ResponseEntity.ok(imagenService.subirImagenes(imagenes));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarImagen(@RequestParam String url) {
        try {
            return ResponseEntity.ok(imagenService.eliminarImagen(url));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}