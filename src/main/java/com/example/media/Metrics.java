
package com.example.media;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Metrics {
    private static final ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    public static void increment(String name) {
        counters.computeIfAbsent(name, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public static String snapshot() {
        StringBuilder sb = new StringBuilder();
        for (var entry : counters.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue().get()).append("\n");
        }
        return sb.toString();
    }
}
