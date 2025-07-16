package com.example.media;

import java.util.*;
import java.sql.*;

public class SqliteDAO implements JobDAO {
    private final Connection conn;

    public SqliteDAO(Connection conn) throws SQLException {
        this.conn = conn;
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS jobs (" +
                    "id TEXT PRIMARY KEY, " +
                    "filename TEXT NOT NULL, " +
                    "status TEXT NOT NULL DEFAULT 'queued', " +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");
        }
    }

    @Override
    public void insert(Job job) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO jobs (id, filename, status) VALUES (?, ?, ?)")) {
            ps.setString(1, job.id);
            ps.setString(2, job.output);
            ps.setString(3, job.status());
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.log("Failed to insert job: " + e.getMessage());
        }
    }

    @Override
    public Job findById(String id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM jobs WHERE id = ?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Job(
                    rs.getString("id"),
                    rs.getString("filename"),
                    rs.getString("status")
                );
             }
        } catch (SQLException e) {
            Logger.log("Failed to query job: " + e.getMessage());
        }
        return null;
    }
    @Override
    public void updateStatus(String jobId, String newStatus) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE jobs SET status = ? WHERE id = ?")) {
            ps.setString(1, newStatus);
            ps.setString(2, jobId);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                Logger.log("No job found with ID: " + jobId);
            }
        } catch (SQLException e) {
            Logger.log("Failed to insert job: " + e.getMessage());
        }
    }
	public List<Job> findByStatus(String status) {
		List<Job> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM jobs WHERE status = ?")) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add( new Job(
                    rs.getString("id"),
                    rs.getString("filename"),
                    rs.getString("status")
				));
             }
        } catch (SQLException e) {
            Logger.log("Failed to fetch job by status: " + e.getMessage());
        }
        return result;
    }
}
