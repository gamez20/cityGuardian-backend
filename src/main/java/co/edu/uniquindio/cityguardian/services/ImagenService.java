package co.edu.uniquindio.cityguardian.services;

import co.edu.uniquindio.cityguardian.dto.ImagenDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImagenService {
    ImagenDTO subirImagen(MultipartFile imagen) throws Exception;
    String eliminarImagen(String idImagen) throws Exception;
    List<ImagenDTO> subirImagenes(List<MultipartFile> imagenes) throws Exception;
}