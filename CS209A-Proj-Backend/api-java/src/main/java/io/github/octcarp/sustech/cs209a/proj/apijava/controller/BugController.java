package io.github.octcarp.sustech.cs209a.proj.apijava.controller;

import io.github.octcarp.sustech.cs209a.proj.apijava.exception.BadRequestException;
import io.github.octcarp.sustech.cs209a.proj.apijava.exception.ResourceNotFoundException;
import io.github.octcarp.sustech.cs209a.proj.apijava.service.ApiBugService;
import io.github.octcarp.sustech.cs209a.proj.database.entity.BugPO;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound.BugStatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bugs")
public class BugController {

    @Autowired
    private ApiBugService apiBugService;

    @GetMapping("/getAll")
    public List<BugPO> getAllBugs() {
        return apiBugService.getAllBugs();
    }

    @GetMapping("/getByName")
    public BugPO getBugByName(
        @RequestParam("bug_name") String bugName
    ) {
        return apiBugService.getBugByName(bugName);
    }

    @GetMapping("/statistics")
    public BugStatisticsVO getBugStatistics(
        @RequestParam("bug_name") String bugName
    ) {
        BugStatisticsVO bugStatisticsVO = apiBugService.getBugStatisticsByName(bugName);
        if (bugStatisticsVO == null) {
            throw new ResourceNotFoundException("Bug not found");
        }
        return bugStatisticsVO;
    }

    @GetMapping("/top-frequency")
    public List<BugPO> getTopFrequencyBugs(
        @RequestParam(required = false, defaultValue = "10") Integer limit
    ) {
        if (limit == null) {
            limit = 10;
        }
        if (limit <= 0) {
            throw new BadRequestException("Invalid limit");
        }
        return apiBugService.getTopFrequencyBugs(limit);
    }
}
