package db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.net.httpserver.HttpExchange;

public class bookDB {

    public static ArrayList<book> books;

    static{
        File file = new File("books.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            books = objectMapper.readValue(file, new TypeReference<ArrayList<book>>(){});
        } catch (IOException e) {
            System.out.print("db error: " + e);
        }
    }

    private static void serializeBooks() throws IOException {
        if(books == null || books.isEmpty())return;
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        try {
            writer.writeValue(new File("books.json"), books);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getAllBooks(HttpExchange t)throws Exception{
        if(books == null || books.isEmpty()){
            t.sendResponseHeaders(204, -1);
            return "";
        }
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(books);
        t.sendResponseHeaders(200, json.length());
        return json;
    }

    public static String getSingleBook(int bookID, HttpExchange t)throws Exception{
        if(books == null || books.isEmpty()){
            t.sendResponseHeaders(204, -1);
            return "";
        }
        String ret = "";
        for(var i:books) {
            if(i.bookID != bookID)continue;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(books);
            t.sendResponseHeaders(200, json.length());
        }
        return ret;
    }

    public static String deleteBook(int bookID, HttpExchange t)throws Exception{
        if(books == null || books.isEmpty()){
            t.sendResponseHeaders(204, -1);
            return "";
        }
        String ret = "";
        for(var i:books){
            if(i.bookID != bookID)continue;
            books.remove(i);
            ret = new String("Book: " + i.title + ", ID: " + bookID + " has been successfully deleted");
            t.sendResponseHeaders(200, ret.length());
            serializeBooks();
            return ret;
        }

        t.sendResponseHeaders(204, -1);
        serializeBooks();
        return ret;
    }

}
