package com.example.media;

import java.time.LocalDateTime;

import java.util.*;

public interface JobDAO {
    void insert(Job job);
    Job findById(String id);
    void updateStatus(String jobId, String newStatus);
	void updateTimestamp(String jobId, String when, LocalDateTime ts);
	List<Job> findByStatus(String status);
	List<Job> findAll();
	List<Job> findAllToday();
}
