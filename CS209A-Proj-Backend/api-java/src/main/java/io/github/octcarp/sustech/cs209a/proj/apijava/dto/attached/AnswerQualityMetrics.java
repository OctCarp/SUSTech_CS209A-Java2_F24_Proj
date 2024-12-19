package io.github.octcarp.sustech.cs209a.proj.apijava.dto.attached;

import lombok.Data;

@Data
public class AnswerQualityMetrics {
    private Long responseTimeSeconds;
    private Integer answerLength;
    private Integer ownerReputation;
    private Integer ownerAcceptRate;
}
