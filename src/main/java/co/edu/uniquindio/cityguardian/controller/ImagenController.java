package co.edu.uniquindio.cityguardian.controller;

import co.edu.uniquindio.cityguardian.dto.ImagenDTO;
import co.edu.uniquindio.cityguardian.services.ImagenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/imagenes")
public class ImagenController {

    private final ImagenService imagenService;

    public ImagenController(ImagenService imagenService) {
        this.imagenService = imagenService;
    }

    @PostMapping("/subir")
    public ResponseEntity<ImagenDTO> subirImagen(@RequestPart("imagen") MultipartFile imagen) {
        try {
            return ResponseEntity.ok(imagenService.subirImagen(imagen));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarImagen(@PathVariable String id) {
        try {
            return ResponseEntity.ok(imagenService.eliminarImagen(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}