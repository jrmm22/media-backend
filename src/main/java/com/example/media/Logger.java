
package com.example.media;

public class Logger {
    public static void log(String msg) {
        System.out.printf("[%d] %s%n", System.currentTimeMillis(), msg);
    }
}
