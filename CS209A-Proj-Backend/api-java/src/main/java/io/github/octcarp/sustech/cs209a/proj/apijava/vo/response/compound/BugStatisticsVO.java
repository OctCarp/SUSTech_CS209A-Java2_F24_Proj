package io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound;

import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.basic.attached.BugStatistics;
import io.github.octcarp.sustech.cs209a.proj.database.entity.BugPO;
import lombok.Data;

@Data
public class BugStatisticsVO {
    private BugPO bug;

    private BugStatistics bugStatistics;

    public BugStatisticsVO(BugPO bug, BugStatistics bugStatistics) {
        this.bug = bug;
        this.bugStatistics = bugStatistics;
    }
}