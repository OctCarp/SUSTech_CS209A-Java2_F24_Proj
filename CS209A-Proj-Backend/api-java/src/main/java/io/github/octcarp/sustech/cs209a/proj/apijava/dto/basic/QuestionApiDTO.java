package io.github.octcarp.sustech.cs209a.proj.apijava.dto.basic;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionApiDTO{
    private Long questionId;
    private Long ownerId;
    private String title;
    private String body;
    private LocalDateTime creationDate;
    private LocalDateTime lastActivityDate;
    private LocalDateTime lastEditDate;
    private Long acceptedAnswerId;
    private Integer score;
    private Integer viewCount;
    private Integer upVoteCount;
    private Integer downVoteCount;
    private Integer favoriteCount;
}