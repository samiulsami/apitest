package db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONObject;

public class db {

    public static ArrayList<book> books;
    static{
        File file = new File("books.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            books = objectMapper.readValue(file, new TypeReference<ArrayList<book>>(){});
        } catch (IOException e) {
            System.out.print("db error: " + e);
            throw new RuntimeException(e);
        }
    }
    public static String getAllBooks()throws Exception{
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(books);
        return json;
    }
}
