package io.github.octcarp.sustech.cs209a.proj.apijava.dto.basic;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnswerApiDTO {
    private Long answerId;
    private Long questionId;
    private Long ownerId;
    private String body;
    private LocalDateTime creationDate;
    private LocalDateTime lastActivityDate;
    private LocalDateTime lastEditDate;
    private Integer score;
    private Integer upVoteCount;
    private Integer downVoteCount;
    private Boolean isAccepted;
}