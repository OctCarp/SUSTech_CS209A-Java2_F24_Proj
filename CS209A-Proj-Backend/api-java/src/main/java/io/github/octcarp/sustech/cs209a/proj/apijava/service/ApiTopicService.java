package io.github.octcarp.sustech.cs209a.proj.apijava.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.octcarp.sustech.cs209a.proj.apijava.mapper.TopicAnalysisMapper;
import io.github.octcarp.sustech.cs209a.proj.apijava.dto.attached.TopicStatistics;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.basic.TopicBriefVO;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound.TopicEngagementVO;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound.TopicStatisticsVO;
import io.github.octcarp.sustech.cs209a.proj.database.entity.TopicPO;
import io.github.octcarp.sustech.cs209a.proj.database.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiTopicService {

    @Autowired
    private TopicAnalysisMapper topicAnalysisMapper;

    @Autowired
    private TopicService topicService;

    public TopicPO getTopicByName(String topicName) {
        return topicService.getTopicByName(topicName);
    }

    public List<TopicBriefVO> getAllTopicsBrief() {
        return topicService.list().parallelStream()
            .map(topic -> new TopicBriefVO(topic.getTopicId(), topic.getTopicName()))
            .collect(Collectors.toList());
    }

    public TopicStatisticsVO getTopicStatisticsByName(
        String topicName,
        LocalDateTime startTime,
        LocalDateTime endTime
    ) {
        TopicPO topicPO = getTopicByName(topicName);
        if (topicPO == null) {
            return null;
        }

        TopicStatistics topicStatistics = topicAnalysisMapper.getTopicStatisticsById(topicPO.getTopicId(), startTime, endTime);
        return new TopicStatisticsVO(topicPO, topicStatistics);
    }

    public List<TopicEngagementVO> getTopEngagementTopics(Long limit, Long minReputation) {
        List<TopicEngagementVO> allTopicEngagement = topicAnalysisMapper.getAllTopicEngagement(minReputation);

        return allTopicEngagement.stream()
            .sorted(Comparator.comparing(TopicEngagementVO::calculateScore).reversed())
            .limit(limit)
            .collect(Collectors.toList());

    }

    public List<TopicPO> getTopFrequencyTopics(Long limit) {
        QueryWrapper<TopicPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("frequency");

        queryWrapper.last("LIMIT " + limit);

        return topicService.getBaseMapper().selectList(queryWrapper);
    }
}
