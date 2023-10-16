package org.example;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import org.json.JSONObject;

import com.sun.net.httpserver.*;

public class Main {

    public static void main(String[] args) throws Exception {
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
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "heeh");
                jsonObject.put("age", 23);
                jsonObject.put("married", "asdsa");
                String response = jsonObject.toString();
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