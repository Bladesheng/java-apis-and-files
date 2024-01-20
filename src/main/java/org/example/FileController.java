package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileController {
    public static void saveToFile(String filename, String textToSave) {
        try {
            Path path = Path.of(filename);

            boolean exists = Files.exists(path);
            if (!exists) {
                Path newFile = Files.createFile(path);
                System.out.println("created new file: " + newFile);
            }

            Files.writeString(path, textToSave);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
