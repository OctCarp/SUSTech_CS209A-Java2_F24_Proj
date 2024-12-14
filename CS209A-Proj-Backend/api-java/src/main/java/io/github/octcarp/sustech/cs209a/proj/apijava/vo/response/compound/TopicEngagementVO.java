package io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound;

public class TopicEngagementVO {
    private String topic;
    private Integer engagement;

    public TopicEngagementVO(String topic, Integer engagement) {
        this.topic = topic;
        this.engagement = engagement;
    }
}