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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import db.bookDB;
import db.book;
import com.sun.net.httpserver.*;

public class Main {

    public static void main(String[] args)throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        HttpContext _books = server.createContext("/bookstore/books", new bookHandler());
      //  HttpContext _authors = server.createContext("/bookstore/authors", new authorHandler());
       /* _books.setAuthenticator(new BasicAuthenticator("myrealm") {
            @Override
            public boolean checkCredentials(String user, String pwd) {
                return user.equals("admin") && pwd.equals("admin");
            }
        });*/

        server.setExecutor(null); // creates a default executor
        server.start();
    }

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
    static class bookHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if("GET".equals(t.getRequestMethod())) {
                String response = null;
                int id = getSuffix(t.getRequestURI().toString());

                if(id < 0) {///Get all books
                    try {
                        response = bookDB.getAllBooks(t);
                    } catch (Exception e) {
                        System.out.println("Error in GET: " + e);
                        throw new RuntimeException(e);
                    }
                }
                else{/// Get one book
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
            }
            else if("DELETE".equals(t.getRequestMethod())){
                String response = null;
                int id = getSuffix(t.getRequestURI().toString());

                try {
                    response = bookDB.deleteBook(id, t);
                } catch (Exception e) {
                    System.out.println("Error in DELETE: " + e);
                    throw new RuntimeException(e);
                }

                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            else if("POST".equals(t.getRequestMethod())){
                String response;
                try{
                    response = bookDB.addBook(t);
                }catch (Exception e){
                    System.out.println("Error in POST: " + e);
                    throw new RuntimeException(e);
                }
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            else{
                t.sendResponseHeaders(405,-1);
            }
            t.close();
        }
    }

    static class authorHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if("GET".equals(t.getRequestMethod())) {
                //System.out.println(t.getRequestURI());
                String response = null;
                try {
                    response = bookDB.getAllBooks(t);
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                    throw new RuntimeException(e);
                }
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            else{
                t.sendResponseHeaders(405,-1);
            }
            t.close();
        }
    }

}