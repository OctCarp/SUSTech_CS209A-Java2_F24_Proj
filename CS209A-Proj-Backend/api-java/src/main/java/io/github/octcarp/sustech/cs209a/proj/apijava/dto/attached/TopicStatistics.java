package io.github.octcarp.sustech.cs209a.proj.apijava.dto.attached;

import lombok.Data;

@Data
public class TopicStatistics {
    private Long totalPosts;
    private Long questionCount;
    private Long answerCount;
    private Long commentCount;
    private Long userCount;
    private Long totalViews;
    private Long totalFavorites;
}
