package io.github.octcarp.sustech.cs209a.proj.apijava.dto.attached;

import lombok.Data;

@Data
public class TopicEngagement {
    Long questionCount;
    Long answerCount;
    Long commentCount;

    Long totalUpVotes;
    Long totalDownVotes;
    Long totalEngagement;

    public Long getScore() {
        if (totalEngagement == null) {
            totalEngagement = (totalUpVotes * 2 - totalDownVotes)
                + questionCount * 15 + answerCount * 10 + commentCount * 5;
        }

        return getTotalEngagement();
    }
}
