package org.example;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import db.db;
import db.book;
import com.sun.net.httpserver.*;

public class Main {

    public static void main(String[] args)throws Exception {
       /* ArrayList<book> books = new ArrayList<book>();
        for(int i=0; i<5; i++){
            String title = "book " + i;
            String authorName = "author " + i;
            int bookID = i;
            int authorID = i;
            int pages = 10+i;

            books.add(new book(title, authorName, bookID, authorID, pages));
        }

        {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            writer.writeValue(new File("books.json"), books);
            String json = mapper.writeValueAsString(books);
            System.out.println(json);
        }*/

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        HttpContext testcontext = server.createContext("/bookstore/books", new MyHandler());

      /*  testcontext.setAuthenticator(new BasicAuthenticator("myrealm") {
            @Override
            public boolean checkCredentials(String user, String pwd) {
                return user.equals("admin") && pwd.equals("admin");
            }
        });*/

        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if("GET".equals(t.getRequestMethod())) {

                String response = null;
                try {
                    response = db.getAllBooks();
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                    throw new RuntimeException(e);
                }

                t.sendResponseHeaders(200, response.length());
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