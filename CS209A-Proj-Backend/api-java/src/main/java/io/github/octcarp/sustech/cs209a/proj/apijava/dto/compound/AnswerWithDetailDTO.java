package io.github.octcarp.sustech.cs209a.proj.apijava.dto.compound;

import io.github.octcarp.sustech.cs209a.proj.apijava.dto.attached.AnswerQualityLevel;
import io.github.octcarp.sustech.cs209a.proj.apijava.dto.attached.AnswerQualityMetrics;
import io.github.octcarp.sustech.cs209a.proj.apijava.dto.basic.AnswerApiDTO;
import io.github.octcarp.sustech.cs209a.proj.apijava.dto.basic.QuestionApiDTO;
import io.github.octcarp.sustech.cs209a.proj.apijava.dto.basic.UserApiDTO;
import lombok.Data;

import java.time.temporal.ChronoUnit;

@Data
public class AnswerWithDetailDTO {
    private AnswerApiDTO answer;
    private QuestionApiDTO question;
    private UserApiDTO owner;


    private AnswerQualityLevel level;

    private AnswerQualityMetrics metrics;

    public void analyze() {
        if (metrics == null) {
            defineMetrics();
        }
        if (level == null) {
            defineLevel();
        }
    }

    private void defineMetrics() {
        metrics = new AnswerQualityMetrics();
        metrics.setResponseTimeSeconds(question.getCreationDate().until(answer.getCreationDate(), ChronoUnit.SECONDS));
        metrics.setAnswerLength(answer.getBody().length());
        if (owner == null) {
            metrics.setOwnerReputation(-1);
            metrics.setOwnerAcceptRate(-1);
        } else {
            metrics.setOwnerReputation(owner.getReputation());
            metrics.setOwnerAcceptRate(owner.getAcceptRate());
        }
    }

    private void defineLevel() {
        int totalVotes = answer.getUpVoteCount() + answer.getDownVoteCount();
        double upvoteRate = totalVotes > 0 ? ((double) answer.getUpVoteCount() / totalVotes) : 0;

        if (metrics.getResponseTimeSeconds() < 20) {
            level = AnswerQualityLevel.LOW;
            return;
        }

        if (answer.getIsAccepted() || upvoteRate > 0.8) {
            level = AnswerQualityLevel.HIGH;
            return;
        }

        level = upvoteRate > 0.5 ? AnswerQualityLevel.MEDIUM : AnswerQualityLevel.LOW;
    }
}
