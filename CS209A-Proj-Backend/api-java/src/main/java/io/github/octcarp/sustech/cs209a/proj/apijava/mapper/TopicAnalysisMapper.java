package io.github.octcarp.sustech.cs209a.proj.apijava.mapper;

import io.github.octcarp.sustech.cs209a.proj.apijava.dto.attached.TopicStatistics;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.basic.TopicFreqVO;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound.TopicEngagementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TopicAnalysisMapper {
    TopicStatistics getTopicStatisticsById(
        @Param("topicId") Long topicId,
        @Param("startTime") LocalDateTime startSeconds,
        @Param("endTime") LocalDateTime endSeconds
    );

//    TopicEngagement getTopicEngagementById(
//        @Param("topicId") Long topicId,
//        @Param("minReputation") Long minReputation
//    );

    List<TopicEngagementVO> getAllTopicEngagement(
        @Param("minReputation") Long minReputation
    );

    List<TopicFreqVO> getTopFrequencyTopicsWithTime(
        @Param("limit") Long limit,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
}
