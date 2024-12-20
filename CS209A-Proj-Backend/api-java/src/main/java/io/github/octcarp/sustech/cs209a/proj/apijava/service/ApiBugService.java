package io.github.octcarp.sustech.cs209a.proj.apijava.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.octcarp.sustech.cs209a.proj.apijava.mapper.BugAnalysisMapper;
import io.github.octcarp.sustech.cs209a.proj.apijava.dto.attached.BugStatistics;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.basic.BugBriefVO;
import io.github.octcarp.sustech.cs209a.proj.database.entity.BugPO;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound.BugStatisticsVO;
import io.github.octcarp.sustech.cs209a.proj.database.enums.BugType;
import io.github.octcarp.sustech.cs209a.proj.database.service.BugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiBugService {
    @Autowired
    private BugAnalysisMapper bugAnalysisMapper;

    @Autowired
    private BugService bugService;

    public BugPO getBugByName(String bugName) {
        BugPO bugPO = bugService.getBugByName(bugName);
        if (bugPO == null || bugPO.getBugId() == 0) {
            return null;
        }
        return bugPO;
    }

    public List<BugPO> getAllBugs() {
        return bugService.list();
    }

    public BugStatisticsVO getBugStatisticsByName(String bugName) {
        BugPO bugPO = getBugByName(bugName);
        if (bugPO == null) {
            return null;
        }
        BugStatistics bugStatistics = bugAnalysisMapper.getBugStatisticsById(bugPO.getBugId());
        return new BugStatisticsVO(bugPO, bugStatistics);
    }

    public List<BugPO> getTopFrequencyBugs(Integer limit, BugType bugType) {
        QueryWrapper<BugPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("bug_frequency");

        if (bugType != null) {
            queryWrapper.eq("bug_type", bugType);
        }

        queryWrapper.last("LIMIT " + limit);

        return bugService.getBaseMapper().selectList(queryWrapper);
    }

    public List<BugBriefVO> getTopFrequencyBugsBrief(Integer limit, BugType bugType) {
        List<BugPO> bugPOList = getTopFrequencyBugs(limit, bugType);
        return bugPOList
            .parallelStream()
            .map(
                bugPO -> new BugBriefVO(bugPO.getBugId(), bugPO.getBugName())
            ).collect(Collectors.toList());
    }
}
