import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MyService {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/json", new Handler("{\"name\":\"%s\", \"timestamp\":\"%s\"}", "application/json"));
        server.createContext("/html", new Handler("<body><name>%s</name><timestamp>%s</timestamp></body>", "text/html"));
        server.setExecutor(null);
        server.start();
    }
}

class Handler implements HttpHandler {
    private String template;
    private String contentType;
    public Handler(String template, String contentType) {
         super();
         this.template = template;
         this.contentType = contentType;
    }
    
    @Override
    public void handle(HttpExchange t) throws IOException {
        String name  = t.getRequestURI().getQuery().toLowerCase();
        String response = String.format(template, name, System.currentTimeMillis());
        System.out.println(response);
        t.getResponseHeaders().add("Content-Type", contentType);
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
