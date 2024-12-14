package io.github.octcarp.sustech.cs209a.proj.apijava.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.octcarp.sustech.cs209a.proj.apijava.mapper.TopicAnalysisMapper;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.basic.attached.TopicStatistics;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound.TopicStatisticsVO;
import io.github.octcarp.sustech.cs209a.proj.database.entity.TopicPO;
import io.github.octcarp.sustech.cs209a.proj.database.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiTopicService {

    @Autowired
    private TopicAnalysisMapper topicAnalysisMapper;

    @Autowired
    private TopicService topicService;

    public TopicPO getTopicByName(String topicName) {
        return topicService.getTopicByName(topicName);
    }

    public TopicStatisticsVO getTopicStatisticsByName(String topicName) {
        TopicPO topicPO = getTopicByName(topicName);
        if (topicPO == null) {
            return null;
        }

        TopicStatistics topicStatistics = topicAnalysisMapper.getTopicStatisticsById(topicPO.getTopicId());
        return new TopicStatisticsVO(topicPO, topicStatistics);
    }

    public List<TopicPO> getTopFrequencyTopics(Integer limit) {
        QueryWrapper<TopicPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("frequency");

        queryWrapper.last("LIMIT " + limit);

        return topicService.getBaseMapper().selectList(queryWrapper);
    }
}
