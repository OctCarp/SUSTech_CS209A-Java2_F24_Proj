package io.github.octcarp.sustech.cs209a.proj.apijava.controller;

import io.github.octcarp.sustech.cs209a.proj.apijava.exception.BadRequestException;
import io.github.octcarp.sustech.cs209a.proj.apijava.service.ApiTopicService;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound.TopicEngagementVO;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound.TopicStatisticsVO;
import io.github.octcarp.sustech.cs209a.proj.database.entity.TopicPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private ApiTopicService apiTopicService;

    @GetMapping("/getByName")
    public TopicPO getTopicByName(
        @RequestParam("topic_name") String topicName
    ) {
        return apiTopicService.getTopicByName(topicName);
    }

    @GetMapping("/statistics")
    public TopicStatisticsVO getTopicStatistics(
        @RequestParam("topic_name") String topicName
    ) {
        return apiTopicService.getTopicStatisticsByName(topicName);
    }

    @GetMapping("/top/engagement")
    public List<TopicEngagementVO> getTopEngagementTopics(
        @RequestParam(required = false, defaultValue = "10") Integer limit,
        @RequestParam(name = "min_reputation", required = false, defaultValue = "0") Integer minReputation
    ) {
        return null;
    }

    @GetMapping("/top/frequency")
    public List<TopicPO> getTopFrequencyTopics(
        @RequestParam(required = false, defaultValue = "10") Integer limit
    ) {
        if (limit <= 0) {
            throw new BadRequestException("Invalid limit");
        }
        return apiTopicService.getTopFrequencyTopics(limit);
    }
}