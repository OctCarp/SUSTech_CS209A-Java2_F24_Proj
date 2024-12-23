package io.github.octcarp.sustech.cs209a.proj.apijava.mapper;

import io.github.octcarp.sustech.cs209a.proj.apijava.dto.basic.UserReputationDistribution;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserAnalysisMapper {

    List<UserReputationDistribution> getUserReputationDistribution();
}
