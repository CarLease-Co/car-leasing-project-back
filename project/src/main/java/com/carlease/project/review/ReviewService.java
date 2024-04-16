package com.carlease.project.review;

import java.util.List;

public interface ReviewService {
    List<Review> findAll();

    Review findById(long id);
}
