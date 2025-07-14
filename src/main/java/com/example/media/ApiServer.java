
package com.example.media;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class ApiServer {
    public static void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/metrics", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response = Metrics.snapshot();
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        });

        server.createContext("/trigger", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String response;

            if( query == null
                || !query.contains("file=") )
            {
                response = "Missing file= parameter!\n" + "query: " + query + "\n";
                exchange.sendResponseHeaders(400, response.length());
            }
            else
            {
                String filename = query.split("file=")[1].split("&")[0];
                String jobId = "job-" + System.currentTimeMillis(); // crude unique ID

                Job job = new Job(jobId, filename);
                JobOrchestrator.pool.submit(new EncoderWorker(job));

                response = "Submitted job: " + jobId + " for file: " + filename + "\n";
                exchange.sendResponseHeaders(200, response.length());
            }
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });

        server.setExecutor(null); // use default executor
        server.start();

        Logger.log("HTTP server started on port 8080");
    }
}
