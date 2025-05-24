package co.edu.uniquindio.cityguardian.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import co.edu.uniquindio.cityguardian.services.ImagenService;
import co.edu.uniquindio.cityguardian.dto.ImagenDTO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class ImagenServiceImpl implements ImagenService {

    private final Cloudinary cloudinary;

    public ImagenServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
    private File convertir(MultipartFile imagen) throws IOException {
        File file = File.createTempFile(imagen.getOriginalFilename(), null);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(imagen.getBytes());
        fos.close();
        return file;
    }

    @Override
    public ImagenDTO subirImagen(MultipartFile imagen) throws Exception {
        File file = convertir(imagen);
        Map resultado = cloudinary.uploader().upload(file, ObjectUtils.asMap(
                "folder", "CityGuardian",
                "resource_type", "auto"
        ));
        file.delete();

        String url = (String) resultado.get("secure_url"); // usando secure_url en lugar de url
        String publicId = (String) resultado.get("public_id");

        if (url == null || publicId == null) {
            throw new Exception("Error al obtener la URL o ID de la imagen");
        }

        return new ImagenDTO(url, publicId);
    }

    @Override
    public String eliminarImagen(String idImagen) throws Exception {
        Map resultado = cloudinary.uploader().destroy(idImagen, ObjectUtils.emptyMap());
        String estado = (String) resultado.get("result");

        if (!"ok".equals(estado)) {
            throw new Exception("No se pudo eliminar la imagen");
        }

        return "Imagen eliminada correctamente";
    }
}