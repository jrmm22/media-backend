
package com.example.media;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.*;

import java.util.*;
import java.time.LocalDateTime;

public class ApiServer {

    public static void start(JobDAO jobDao) throws IOException, SQLException {
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
        
		server.createContext("/list", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                //String response = Metrics.snapshot();

				List<Job> allJobs = jobDao.findAll();
        		StringBuilder sb = new StringBuilder();
        		for (var job : allJobs) {
		            sb.append(job.id()).append(": ").append(job.input()).append(" Status ").append(job.status())
					  .append(" created: ").append(job.created_at())
					  .append(" started: ").append(job.started_at())
					  .append(" finished: ").append(job.finished_at())
					  .append("\n");
		        }
				byte[] bytes = sb.toString().getBytes();
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
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
                jobDao.insert(job); // persist to SQLite
				jobDao.updateTimestamp(job.id(), "created_at", LocalDateTime.now() );
                //JobOrchestrator.pool.submit(new EncoderWorker(job));

                response = "Submitted job: " + jobId + " for file: " + filename + "\n";
                exchange.sendResponseHeaders(200, response.length());
            }
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });

        server.createContext("/status", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String response;

            if (query == null || !query.contains("job=")) {
                response = "Missing job= parameter!\n";
                exchange.sendResponseHeaders(400, response.length());
            } else {
                String jobId = query.split("job=")[1].split("&")[0];
                Job job = jobDao.findById(jobId);

                if (job == null) {
                    response = "No such job: " + jobId + "\n";
                    exchange.sendResponseHeaders(404, response.length());
                } else {
                    response = String.format("Job ID: %s\nInput: %s\nStatus: %s\n", job.id(), job.input(), job.status());
                    exchange.sendResponseHeaders(200, response.length());
                }
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
