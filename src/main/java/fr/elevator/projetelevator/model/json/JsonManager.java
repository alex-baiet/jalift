package fr.elevator.projetelevator.model.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class JsonManager {
    private static ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .enable(JsonParser.Feature.ALLOW_COMMENTS);

    /** Lis un fichier Json et renvoie un objet au format demandé. */
    public static <T> T readJson(String path, Class<T> tClass) {
        try {
            return mapper.readValue(new File(path), tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Ecrit l'objet au format Json dans le fichier indiqué. */
    public static void writeJson(String path, Object obj) {
        try {
            mapper.writeValue(new File(path), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
