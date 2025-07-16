
package com.example.media;

import java.util.concurrent.*;
import java.io.IOException;

import java.sql.*;

public class JobOrchestrator {
    static JobDAO jobDao;
    public static final ExecutorService pool = Executors.newFixedThreadPool(100);

    public static void main(String[] args) throws InterruptedException, IOException, SQLException  {

        Connection conn = DriverManager.getConnection("jdbc:sqlite:jobs.db");
        jobDao = new SqliteDAO(conn);
		new Thread(new JobDispatcher(jobDao)).start();
        ApiServer.start(jobDao);

    }
}
