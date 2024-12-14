package io.github.octcarp.sustech.cs209a.proj.apijava.mapper;

import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.basic.attached.BugStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BugAnalysisMapper {
    BugStatistics getBugStatisticsById(@Param("bugId") Long bugId);
}
