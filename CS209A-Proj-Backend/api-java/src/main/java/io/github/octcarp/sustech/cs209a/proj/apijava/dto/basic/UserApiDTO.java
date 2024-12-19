package io.github.octcarp.sustech.cs209a.proj.apijava.dto.basic;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserApiDTO {
    private Long userId;
    private String displayName;
    private LocalDateTime creationDate;
    private Integer reputation;
    private Integer acceptRate;
    private Integer upVoteCount;
    private Integer downVoteCount;
}