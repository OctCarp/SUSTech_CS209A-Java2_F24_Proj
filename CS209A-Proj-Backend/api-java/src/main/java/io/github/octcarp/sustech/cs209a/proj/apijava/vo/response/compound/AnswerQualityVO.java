package io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound;

import io.github.octcarp.sustech.cs209a.proj.database.entity.AnswerPO;
import io.github.octcarp.sustech.cs209a.proj.database.entity.QuestionPO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
public class AnswerQualityVO {
    private QuestionPO question;

    private AnswerPO answer;

    private QualityMetrics metrics;
    private QualityAssessment assessment;

    public AnswerQualityVO(QuestionPO question, AnswerPO answer) {
        this.question = question;
        this.answer = answer;
        defineQualityMetrics();
        defineQualityAssessment();
    }

    public void defineQualityMetrics() {
        metrics = new QualityMetrics();
        metrics.setResponseTimeSeconds(question.getCreationDate().until(answer.getCreationDate(), ChronoUnit.SECONDS));
        metrics.setIsAccepted(answer.isAccepted());
    }

    public void defineQualityAssessment() {
        assessment = new QualityAssessment();
        // Todo: Implement quality assessment logic

    }

    @Data
    public static class QualityMetrics {
        private Long responseTimeSeconds;
        private Boolean isAccepted;
    }

    @Data
    public static class QualityAssessment {
        private Double qualityScore;
        private Boolean isHighQuality;
        private String qualityLevel; // Low, Medium, High
    }
}