package io.github.octcarp.sustech.cs209a.proj.apijava.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.octcarp.sustech.cs209a.proj.apijava.mapper.BugAnalysisMapper;
import io.github.octcarp.sustech.cs209a.proj.apijava.dto.attached.BugStatistics;
import io.github.octcarp.sustech.cs209a.proj.database.entity.BugPO;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound.BugStatisticsVO;
import io.github.octcarp.sustech.cs209a.proj.database.service.BugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiBugService {
    @Autowired
    private BugAnalysisMapper bugAnalysisMapper;

    @Autowired
    private BugService bugService;

    public BugPO getBugByName(String bugName) {
        return bugService.getBugByName(bugName);
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

    public List<BugPO> getTopFrequencyBugs(Integer limit) {
        QueryWrapper<BugPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("bug_frequency");

        queryWrapper.last("LIMIT " + limit);

        return bugService.getBaseMapper().selectList(queryWrapper);
    }
}
