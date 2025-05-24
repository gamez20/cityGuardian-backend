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
import java.util.ArrayList;
import java.util.List;
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
    public List<ImagenDTO> subirImagenes(List<MultipartFile> imagenes) throws Exception {
        List<ImagenDTO> imagenesDTOS = new ArrayList<>();

        for (MultipartFile imagen : imagenes) {
            ImagenDTO imagenDTO = subirImagen(imagen);
            imagenesDTOS.add(imagenDTO);
        }

        return imagenesDTOS;
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

    public String eliminarImagen(String imageUrl) throws Exception {
        try {
            // Extraer el public_id de la URL
            String publicId = extraerPublicIdDeUrl(imageUrl);

            // Eliminar la imagen usando el public_id
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            if (result.get("result").equals("ok")) {
                return "Imagen eliminada correctamente";
            } else {
                throw new Exception("No se pudo eliminar la imagen");
            }
        } catch (Exception e) {
            throw new Exception("Error al eliminar la imagen: " + e.getMessage());
        }
    }

    private String extraerPublicIdDeUrl(String imageUrl) {
        // Ejemplo URL: https://res.cloudinary.com/dfacja0b6/image/upload/v1748052464/CityGuardian/fxgfrniwyo4emnumtwhp.jpg
        String[] partes = imageUrl.split("/");
        // Obtenemos las últimas dos partes (carpeta/nombre)
        String nombreArchivo = partes[partes.length - 1];
        String carpeta = partes[partes.length - 2];
        // Eliminamos la extensión del archivo
        String nombreSinExtension = nombreArchivo.substring(0, nombreArchivo.lastIndexOf('.'));
        // Construimos el public_id
        return carpeta + "/" + nombreSinExtension;
    }
}