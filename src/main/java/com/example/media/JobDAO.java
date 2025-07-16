package com.example.media;

import java.util.*;

public interface JobDAO {
    void insert(Job job);
    Job findById(String id);
    void updateStatus(String jobId, String newStatus);
	List<Job> findByStatus(String status);
}
