package io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound;

import io.github.octcarp.sustech.cs209a.proj.apijava.dto.attached.TopicEngagement;
import lombok.Data;

@Data
public class TopicEngagementVO {
    private Long topicId;
    private String topicName;
    private TopicEngagement topicEngagement;
    private Long engagementScore;

    public Long calculateScore() {
        if (engagementScore == null) {
            engagementScore = topicEngagement.getScore();
        }
        return getEngagementScore();
    }
}