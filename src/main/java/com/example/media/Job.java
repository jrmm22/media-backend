
package com.example.media;

public class Job {
    public final String id;
    public final String input;
    public final String output;
    public String status;

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
    public String id() { return id; }
    public String input() { return input; }
    public String output() { return output; }
    public String status() { return status; }

    public void setStatus(String newStatus) {
        this.status = newStatus;
    }
}
