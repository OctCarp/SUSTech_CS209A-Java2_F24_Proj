package io.github.octcarp.sustech.cs209a.proj.apijava.mapper;

import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.basic.attached.TopicStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TopicAnalysisMapper {
    TopicStatistics getTopicStatisticsById(@Param("topicId") Long topicId);
}
