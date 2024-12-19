package io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound;

import io.github.octcarp.sustech.cs209a.proj.apijava.dto.attached.TopicStatistics;
import io.github.octcarp.sustech.cs209a.proj.database.entity.TopicPO;
import lombok.Data;

@Data
public class TopicStatisticsVO {
    private TopicPO topic;
    private TopicStatistics topicStatistics;

    public TopicStatisticsVO(TopicPO topic, TopicStatistics topicStatistics) {
        this.topic = topic;
        this.topicStatistics = topicStatistics;
    }
}
