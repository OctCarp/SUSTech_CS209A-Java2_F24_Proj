package io.github.octcarp.sustech.cs209a.proj.apijava.controller;

import io.github.octcarp.sustech.cs209a.proj.apijava.exception.BadRequestException;
import io.github.octcarp.sustech.cs209a.proj.apijava.service.ApiTopicService;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound.TopicEngagementVO;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound.TopicStatisticsVO;
import io.github.octcarp.sustech.cs209a.proj.database.entity.TopicPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private ApiTopicService apiTopicService;

    @GetMapping("/getAll")
    public List<TopicPO> getAllTopics() {
        return apiTopicService.getAllTopics();
    }

    @GetMapping("/getByName")
    public TopicPO getTopicByName(
        @RequestParam("topic_name") String topicName
    ) {
        return apiTopicService.getTopicByName(topicName);
    }

    @GetMapping("/statistics")
    public TopicStatisticsVO getTopicStatistics(
        @RequestParam("topic_name") String topicName,
        @RequestParam(value = "start_time", required = false, defaultValue = "0") Long startMillisecond,
        @RequestParam(value = "end_time", required = false, defaultValue = "1735660800") Long endMillisecond
    ) {
        LocalDateTime startDateTime, endDateTime;
        startDateTime = LocalDateTime.ofEpochSecond(startMillisecond, 0, ZoneOffset.UTC);
        endDateTime = LocalDateTime.ofEpochSecond(endMillisecond, 0, ZoneOffset.UTC);
        return apiTopicService.getTopicStatisticsByName(topicName, startDateTime, endDateTime);
    }

    @GetMapping("/top/engagement")
    public List<TopicEngagementVO> getTopEngagementTopics(
        @RequestParam(required = false, defaultValue = "10") Long limit,
        @RequestParam(name = "min_reputation", required = false, defaultValue = "0") Long minReputation
    ) {
        return apiTopicService.getTopEngagementTopics(limit, minReputation);
    }

    @GetMapping("/top/frequency")
    public List<TopicPO> getTopFrequencyTopics(
        @RequestParam(required = false, defaultValue = "10") Long limit
    ) {
        if (limit <= 0) {
            throw new BadRequestException("Invalid limit");
        }
        return apiTopicService.getTopFrequencyTopics(limit);
    }
}