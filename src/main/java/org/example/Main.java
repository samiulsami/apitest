package org.example;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import auth.JWTtoken;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import db.bookDB;
import db.book;
import com.sun.net.httpserver.*;

public class Main {
    private static String my_username = "sami";
    private static String my_password = "1234";
    private static final int TokenLifeSpanMinutes = 10;

    public static void main(String[] args)throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        HttpContext _books = server.createContext("/bookstore/books", new bookHandler());
        HttpContext _login = server.createContext("/bookstore/login", new loginHandler());

        _login.setAuthenticator(new BasicAuthenticator("myrealm") {
            @Override
            public boolean checkCredentials(String user, String pwd) {
                return user.equals(my_username) && pwd.equals(my_password);
            }
        });

        server.setExecutor(null); // creates a default executor
        server.start();
    }

    ///Returns the integer at the end of an URI. Returns -1 if it doesn't exist
    ///e.g; getSuffix("http://localhost:8000/bookstore/books/4") returns 4
    ///getSuffix("http://localhost:8000/bookstore/books/adasd") returns -1
    private static int getSuffix(String uri){
        String[] slist = uri.split("/",0);
        String tmp = slist[slist.length - 1];
        int num = -1;

        try{
            num = Integer.parseInt(tmp);
        }catch(NumberFormatException e){
            //System.out.println("Invalid URI Suffix");
        }

        return num;
    }

    private static boolean Authenticated(HttpExchange t)throws IOException{
        boolean valid = true;
        var keys = t.getRequestHeaders();
        if(!keys.containsKey("Authorization"))valid = false;

        if(valid){
            try{
                JWTtoken tmpToken = new JWTtoken(keys.get("Authorization").get(0));
                valid = tmpToken.verify();
                if(valid){
                    long seconds = tmpToken.remainingSeconds();
                    System.out.println("TOKEN VALIDITY: " + seconds/60 + " minutes and " + seconds%60 + " seconds remaining");
                }
            }catch (Exception e){
                System.out.println("JWT token authentication error: " + e);
                valid = false;
            }
        }

        if(!valid){
            String response = "This action is unauthorized\nPlease login first";
            t.sendResponseHeaders(403,response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            t.close();
        }

        return valid;
    }

    static class bookHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {

            if(!Authenticated(t)) {
                return;
            }

            switch(t.getRequestMethod()){
                case "GET": {
                    String response = null;
                    int id = getSuffix(t.getRequestURI().toString());

                    if (id < 0) {///Get all books
                        try {
                            response = bookDB.getAllBooks(t);
                        } catch (Exception e) {
                            System.out.println("Error in GET: " + e);
                            throw new RuntimeException(e);
                        }
                    } else {/// Get one book
                        try {
                            response = bookDB.getSingleBook(id, t);
                        } catch (Exception e) {
                            System.out.println("Error in GET: " + e);
                            throw new RuntimeException(e);
                        }
                        // System.out.println(response);
                    }
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    break;
                }
                case "DELETE": {
                    String response = null;
                    int id = getSuffix(t.getRequestURI().toString());

                    if(id==-1){
                        response = "Invalid/Missing ID";
                        t.sendResponseHeaders(400, response.length());
                    }
                    else {
                        try {
                            response = bookDB.deleteBook(id, t);
                        } catch (Exception e) {
                            System.out.println("Error in DELETE: " + e);
                            throw new RuntimeException(e);
                        }
                    }

                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    break;
                }
                case "POST": {
                    String response;

                    try {
                        response = bookDB.addBook(t);
                    } catch (Exception e) {
                        System.out.println("Error in POST: " + e);
                        throw new RuntimeException(e);
                    }

                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    break;
                }
                case "PUT": {
                    String response;
                    int id = getSuffix(t.getRequestURI().toString());

                    try {
                        response = bookDB.updateBook(id, t);
                    } catch (Exception e) {
                        System.out.println("Error in PUT: " + e);
                        throw new RuntimeException(e);
                    }

                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    break;
                }
                default:
                    t.sendResponseHeaders(405, -1);
            }
            t.close();
        }
    }

    static class loginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {

            String response = null;
            try{
                String Base64EncodedCredentials = t.getRequestHeaders().get("Authorization").get(0).split(" ")[1];
                JWTtoken tmp = new JWTtoken(Base64EncodedCredentials, TokenLifeSpanMinutes);
                response = tmp.toString();
            } catch (Exception e) {
                System.out.println("JWT token creation error: " + e);
                throw new RuntimeException(e);
            }

            t.sendResponseHeaders(200, response.length());

            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

            t.close();
        }
    }

}