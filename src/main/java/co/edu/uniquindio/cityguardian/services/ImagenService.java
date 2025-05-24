package co.edu.uniquindio.cityguardian.services;

import co.edu.uniquindio.cityguardian.dto.ImagenDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ImagenService {
    ImagenDTO subirImagen(MultipartFile imagen) throws Exception;
    String eliminarImagen(String idImagen) throws Exception;
}