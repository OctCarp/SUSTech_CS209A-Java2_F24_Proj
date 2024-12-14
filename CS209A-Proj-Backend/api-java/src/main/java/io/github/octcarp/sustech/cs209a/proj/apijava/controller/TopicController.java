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
        @RequestParam String topicName
    ) {
        return apiTopicService.getTopicByName(topicName);
    }

    @GetMapping("/statistics")
    public TopicStatisticsVO getTopicStatistics(
        @RequestParam String topicName
    ) {
        return apiTopicService.getTopicStatisticsByName(topicName);
    }

    @GetMapping("/top/engagement")
    public List<TopicEngagementVO> getTopEngagementTopics(
        @RequestParam Integer limit,
        @RequestParam(required = false) Integer minReputation
    ) {
        return null;
    }

    @GetMapping("/top/frequency")
    public List<TopicPO> getTopFrequencyTopics(
        @RequestParam Integer limit
    ) {
        if (limit == null) {
            limit = 10;
        }
        if (limit <= 0) {
            throw new BadRequestException("Invalid limit");
        }
        return apiTopicService.getTopFrequencyTopics(limit);
    }
}