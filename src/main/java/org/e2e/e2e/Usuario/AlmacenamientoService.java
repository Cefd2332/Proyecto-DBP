package org.e2e.e2e.Usuario;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AlmacenamientoService {

    private final String directorioBase = System.getProperty("user.dir") + "/imagenes"; // Ruta relativa al proyecto

    public String guardarArchivo(MultipartFile archivo) {
        try {
            // Crear el directorio si no existe
            Path directorioPath = Paths.get(directorioBase);
            if (!Files.exists(directorioPath)) {
                Files.createDirectories(directorioPath);
            }

            // Generar un nombre único para el archivo
            String nombreArchivo = System.currentTimeMillis() + "_" + archivo.getOriginalFilename();
            Path archivoPath = directorioPath.resolve(nombreArchivo);

            // Guardar el archivo en el sistema de archivos
            archivo.transferTo(archivoPath.toFile());
            return archivoPath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }
    }
}
