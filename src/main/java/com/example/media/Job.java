
package com.example.media;

import java.sql.*;

public class Job {
    public final String id;
    public final String input;
    public final String output;
    public String status;
	public String timestamp_created;
	public String timestamp_started;
	public String timestamp_finished;

    public Job(String id, String input) {
        //this.id = id;
        //this.input = input;
        //this.output = this.input.replace(".mp4", ".out.mp4");
        //this.status = "pending";
		this(id, input, "pending");
    }
    public Job(String id, String input, String status) {
        this.id = id;
        this.input = input;
        this.output = this.input.replace(".mp4", ".out.mp4");
        this.status = status;
    }
    public Job(String id, String input, String status, String created_at, String started_at, String finished_at) {
		this(id, input, status);
        this.timestamp_created = created_at;
        this.timestamp_started = started_at;
        this.timestamp_finished = finished_at;
    }
	public static Job fromResult( ResultSet result ) throws SQLException {
		return new Job( 
					result.getString("id"),
					result.getString("filename"),
					result.getString("status"),
					result.getString("created_at"),
					result.getString("started_at"),
					result.getString("finished_at")
		);
	}
    public String id() { return id; }
    public String input() { return input; }
    public String output() { return output; }
    public String status() { return status; }
    public String created_at() { return timestamp_created; }
    public String started_at() { return timestamp_started; }
    public String finished_at() { return timestamp_finished; }

    public void setStatus(String newStatus) {
        this.status = newStatus;
    }
}
