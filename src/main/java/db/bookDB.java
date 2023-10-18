package db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.net.httpserver.HttpExchange;

public class bookDB {

    ///Initialized with the contents of books.json
    public static ArrayList<book> books;

    static{
        books = new ArrayList<book>();
        try {
            File file = new File("books.json");
            ObjectMapper objectMapper = new ObjectMapper();
            books = objectMapper.readValue(file, new TypeReference<ArrayList<book>>(){});
        } catch (IOException e) {
            System.out.print("db error: " + e);
        }
    }

    ///Writes the current elements of "books" into "books.json"
    private static void serializeBooks(){
        if(books.isEmpty())return;
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
        if(books.isEmpty()){
            String ret = "Database Empty";
            t.sendResponseHeaders(403,ret.length());
            return ret;
        }
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(books);
        t.sendResponseHeaders(200, json.length());
        return json;
    }

    private static String toJsonFormat(Object b) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(b);
        return json;
    }
    public static String getSingleBook(int bookID)throws Exception{
        if(books.isEmpty()){
            String ret = "Book not found";
            return ret;
        }

        for(var i:books) {
            if(i.bookID != bookID)continue;
            return toJsonFormat(i);
        }

        String ret = "Book not found";
        return ret;
    }
    public static String getSingleBook(int bookID, HttpExchange t)throws Exception{
        String ret = getSingleBook(bookID);
        if(!"Book not found".equals(ret))
            t.sendResponseHeaders(200,ret.length());
        else
            t.sendResponseHeaders(404, ret.length());
        return ret;
    }

    public static String deleteBook(int bookID, HttpExchange t)throws Exception{
        if(books.isEmpty()){
            String ret = "Book not found";
            t.sendResponseHeaders(403,ret.length());
            return ret;
        }
        String ret;
        for(var i:books){
            if(i.bookID != bookID)continue;
            ret = "The following book has been successfully deleted:\n\n" + toJsonFormat(i);
            books.remove(i);
            t.sendResponseHeaders(200, ret.length());
            //serializeBooks();
            return ret;
        }

        ret = "Book not found";
        t.sendResponseHeaders(403,ret.length());
        return ret;
    }

    ///Attempts to parse a book from given headers
    ///Returns List<Object> {book, responseText}
    ///book is null if parse failed
    ///responseText is empty if parse succeeded
    private static List<Object> parseBook(HttpExchange t)throws Exception{
        List<Object> tmp = new ArrayList<>();
        var keys = t.getRequestHeaders();
        //System.out.println(mp.get("dasd").get(0));
        String requiredKeys[] = {"title", "authorName", "authorID", "pages"};
        List<String> missingKeys = new ArrayList<String>();

        for(var k:requiredKeys){
            if(!keys.containsKey(k)) {
                missingKeys.add(k);
            }
        }

        if(!missingKeys.isEmpty()){
            String ret = "Failed to parse book.\nThese keys are missing from the header:\n";
            for(var k:missingKeys){
                ret = ret + "\n-" + k;
            }
            t.sendResponseHeaders(400, ret.length());
            tmp.add(null);
            tmp.add(ret);
            return tmp;
        }

        String title = keys.get("title").get(0);
        String authorName = keys.get("authorName").get(0);
        int pages = Integer.parseInt(keys.get("pages").get(0));
        int authorID = Integer.parseInt(keys.get("authorID").get(0));
        int bookID = getBookID();

        tmp.add(new book(title, authorName, bookID, authorID, pages));
        tmp.add("");
        return tmp;
    }

    public static String updateBook(int id, HttpExchange t)throws Exception{
        int indexToReplace = -1;

        {//Find index of book with bookID = id
            boolean bookFound = false;
            for(int i=0; !bookFound && i<books.size(); i++){
                if(books.get(i).bookID == id){
                    bookFound = true;
                    indexToReplace = i;
                    break;
                }
            }

            if(!bookFound){
                String ret = "Book not found";
                t.sendResponseHeaders(404,ret.length());
                return ret;
            }
        }

        book b;
        {//Replace book
            List<Object> tmp = parseBook(t);
            assert tmp.size() == 2 : " List.size() != 2";
            if(!((String)tmp.get(1)).isEmpty()) return (String)tmp.get(1);
            b = (book) tmp.get(0);
            b.bookID = id;
        }

        String ret = "Old book:\n" + toJsonFormat(books.get(indexToReplace)) + "\n\nReplaced with:\n";
        books.set(indexToReplace, b);
        ret = ret + toJsonFormat(books.get(id));
        t.sendResponseHeaders(200, ret.length());
        return ret;
    }
    public static String addBook(HttpExchange t)throws Exception{
        book b;
        {
            List<Object> tmp = parseBook(t);
            assert tmp.size() == 2 : " List.size() != 2";
            if(!((String)tmp.get(1)).isEmpty()) return (String)tmp.get(1);
            b = (book) tmp.get(0);
        }

        books.add(b);
        String ret = "The following book has been successfully added:\n\n" + toJsonFormat(b);
        t.sendResponseHeaders(200, ret.length());
        return ret;
    }

}
