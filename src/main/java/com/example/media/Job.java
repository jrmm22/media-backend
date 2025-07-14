
package com.example.media;

public class Job {
    public final String id;
    public final String input;
    public final String output;

    public Job(String id, String input) {
        this.id = id;
        this.input = input;
        this.output = this.input.replace(".mp4", ".out.mp4");
    }
}
