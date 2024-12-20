package io.github.octcarp.sustech.cs209a.proj.apijava.controller;

import io.github.octcarp.sustech.cs209a.proj.apijava.exception.BadRequestException;
import io.github.octcarp.sustech.cs209a.proj.apijava.exception.ResourceNotFoundException;
import io.github.octcarp.sustech.cs209a.proj.apijava.service.ApiBugService;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.basic.BugBriefVO;
import io.github.octcarp.sustech.cs209a.proj.database.entity.BugPO;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound.BugStatisticsVO;
import io.github.octcarp.sustech.cs209a.proj.database.enums.BugType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bugs")
public class BugController {

    @Autowired
    private ApiBugService apiBugService;

    @GetMapping("/name/{bug-name}")
    public BugPO getBugByName(
        @PathVariable("bug-name") String bugName
    ) {
        BugPO bugPO = apiBugService.getBugByName(bugName);
        if (bugPO == null) {
            throw new ResourceNotFoundException("Bug not found");
        }
        return bugPO;
    }

    @GetMapping("/name/{bug-name}/statistics")
    public BugStatisticsVO getBugStatistics(
        @PathVariable("bug-name") String bugName
    ) {
        BugStatisticsVO bugStatisticsVO = apiBugService.getBugStatisticsByName(bugName);
        if (bugStatisticsVO == null) {
            throw new ResourceNotFoundException("Bug not found");
        }
        return bugStatisticsVO;
    }

    @GetMapping("/top/frequency")
    public List<BugPO> getTopFrequencyBugs(
        @RequestParam(required = false, defaultValue = "10") Integer limit,
        @RequestParam(required = false) BugType type
    ) {
        if (limit == null) {
            limit = 10;
        }
        if (limit <= 0) {
            throw new BadRequestException("Invalid limit");
        }
        return apiBugService.getTopFrequencyBugs(limit, type);
    }

    @GetMapping("/top/frequency/brief")
    public List<BugBriefVO> getAllBugs(
        @RequestParam(required = false, defaultValue = "50") Integer limit
    ) {
        return apiBugService.getTopFrequencyBugsBrief(limit, null);
    }
}
