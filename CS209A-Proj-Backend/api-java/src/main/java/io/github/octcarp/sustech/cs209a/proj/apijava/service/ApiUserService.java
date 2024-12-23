package io.github.octcarp.sustech.cs209a.proj.apijava.service;

import io.github.octcarp.sustech.cs209a.proj.apijava.dto.basic.UserReputationDistribution;
import io.github.octcarp.sustech.cs209a.proj.apijava.mapper.UserAnalysisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiUserService {
    @Autowired
    private UserAnalysisMapper userAnalysisMapper;

    public List<UserReputationDistribution> getUserReputationDistribution() {
        return userAnalysisMapper.getUserReputationDistribution();
    }
}
