package io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.basic;

import lombok.Data;

@Data
public class BugBriefVO {
    private Long bugId;
    private String bugName;

    public BugBriefVO(Long bugId, String bugName) {
        this.bugId = bugId;
        this.bugName = bugName;
    }
}
