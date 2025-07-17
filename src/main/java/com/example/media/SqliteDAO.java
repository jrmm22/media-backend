package com.example.media;

import java.util.*;
import java.sql.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SqliteDAO implements JobDAO {
    private final Connection conn;
	private static final DateTimeFormatter SQL_TIMESTAMP_FORMAT =
		    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SqliteDAO(Connection conn) throws SQLException {
        this.conn = conn;
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS jobs (" +
                    "id TEXT PRIMARY KEY, " +
                    "filename TEXT NOT NULL, " +
                    "status TEXT NOT NULL DEFAULT 'queued', " +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
					"started_at DATETIME, " +
					"finished_at DATETIME)");
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
                return Job.fromResult(rs);
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
	@Override
    public List<Job> findByStatus(String status) {
		List<Job> result = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM jobs WHERE status = ?")) {
			ps.setString(1, status);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result.add( Job.fromResult( rs ) );
		 	}
		} catch (SQLException e) {
			Logger.log("Failed to find jobs by status: " + e.getMessage());
		}
		return result;
	}
		

	@Override
	public void updateTimestamp(String jobId, String when, LocalDateTime ts) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE jobs set " + when + " = ? WHERE id = ?")) {
            ps.setString(1, ts.format(SQL_TIMESTAMP_FORMAT));
            ps.setString(2, jobId);
            int rows = ps.executeUpdate();
			if (rows == 0) {
				Logger.log("No job found with ID: " + jobId + " for ts update");
			}
        } catch (SQLException e) {
            Logger.log("Failed to fetch job by status: " + e.getMessage());
        }
    }
	@Override
	public List<Job> findAll() {
		List<Job> result = new ArrayList<>();

		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM JOBS")) {
        	ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				result.add( Job.fromResult(rs) );
			}
        } catch (SQLException e) {
			Logger.log("Failed getting a list of all jobs: " + e.getMessage());
        }
		return result;
	}
	public List<Job> findAllToday() {
		List<Job> result = new ArrayList() ;
		// unimplemented
		return result;
	}
	
}
