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

    private static int getBookID(){
        int ret = 0;
        for(var i:books){
            if(ret < i.bookID)ret = i.bookID;
        }
        return ret+1;
    }
    public static String getAllBooks(HttpExchange t)throws Exception{
        if(books == null || books.isEmpty()){
            String ret = "Database Empty";
            t.sendResponseHeaders(403,ret.length());
            return ret;
        }
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(books);
        t.sendResponseHeaders(200, json.length());
        return json;
    }

    public static String getSingleBook(int bookID, HttpExchange t)throws Exception{
        if(books == null || books.isEmpty()){
            String ret = "Book not found";
            t.sendResponseHeaders(403,ret.length());
            return ret;
        }

        for(var i:books) {
            if(i.bookID != bookID)continue;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(i);
            t.sendResponseHeaders(200, json.length());
            return json;
        }
        String ret = "Book not found";
        t.sendResponseHeaders(403,ret.length());
        return ret;
    }

    public static String deleteBook(int bookID, HttpExchange t)throws Exception{
        if(books == null || books.isEmpty()){
            String ret = "Book not found";
            t.sendResponseHeaders(403,ret.length());
            return ret;
        }
        String ret = "";
        for(var i:books){
            if(i.bookID != bookID)continue;
            books.remove(i);
            ret = new String("Book: " + i.title + ", ID: " + bookID + " has been successfully deleted");
            t.sendResponseHeaders(200, ret.length());
            //serializeBooks();
            return ret;
        }

        ret = "Book not found";
        t.sendResponseHeaders(403,ret.length());
        return ret;
    }

    public static String addBook(HttpExchange t)throws Exception{
        var keys = t.getRequestHeaders();
        //System.out.println(mp.get("dasd").get(0));
        if(!keys.containsKey("title") || !keys.containsKey("authorName") || !keys.containsKey("authorID") || !keys.containsKey("pages")){
            String ret = "One or more required keys are missing from the header";
            ret = ret + "\nRequired keys: \ntitle\nauthorname\nauthorID\npages";
            t.sendResponseHeaders(400, ret.length());
            return ret;
        }
        String title = keys.get("title").get(0);
        String authorName = keys.get("authorName").get(0);
        int pages = Integer.parseInt(keys.get("pages").get(0));
        int authorID = Integer.parseInt(keys.get("authorID").get(0));
        int bookID = getBookID();
        books.add(new book(title, authorName, bookID, authorID, pages));

        String ret = "Book: " + title + ", ID: " + bookID + " has been successfully added";
        t.sendResponseHeaders(200, ret.length());
        return ret;
    }

}
