package com.carlease.project.review;

import com.carlease.project.application.Application;
import com.carlease.project.enums.ReviewStatus;
import com.carlease.project.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "reviews")
@Data
public class Review {

    @Column(name = "review_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @Column(name = "review_status")
    @NotNull
    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    @Column(name = "review_date")
    @Temporal(TemporalType.DATE)
    private Date reviewDate;
}
