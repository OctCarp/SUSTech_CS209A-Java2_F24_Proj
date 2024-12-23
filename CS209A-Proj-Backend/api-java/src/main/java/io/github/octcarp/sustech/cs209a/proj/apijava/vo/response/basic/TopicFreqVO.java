package io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.basic;

import lombok.Data;

@Data
public class TopicFreqVO {
    private String topicName;
    private Long frequency;
}
