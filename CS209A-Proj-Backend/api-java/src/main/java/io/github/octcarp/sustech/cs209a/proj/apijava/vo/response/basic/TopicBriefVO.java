package io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.basic;

import lombok.Data;

@Data
public class TopicBriefVO {
    private Long topicId;
    private String topicName;

    public TopicBriefVO(Long topicId, String topicName) {
        this.topicId = topicId;
        this.topicName = topicName;
    }
}
