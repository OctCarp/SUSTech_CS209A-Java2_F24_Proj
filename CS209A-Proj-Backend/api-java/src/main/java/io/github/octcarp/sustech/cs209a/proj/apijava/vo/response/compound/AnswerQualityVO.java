package io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound;

import io.github.octcarp.sustech.cs209a.proj.apijava.dto.attached.AnswerQualityLevel;
import io.github.octcarp.sustech.cs209a.proj.apijava.dto.attached.AnswerQualityMetrics;
import io.github.octcarp.sustech.cs209a.proj.apijava.dto.compound.AnswerWithDetailDTO;
import lombok.Data;

@Data
public class AnswerQualityVO {
    private Long answerId;
    private AnswerQualityLevel level;

    private AnswerQualityMetrics metrics;

    public AnswerQualityVO(AnswerWithDetailDTO answerWithDetailDTO) {
        this.answerId = answerWithDetailDTO.getAnswer().getAnswerId();
        answerWithDetailDTO.analyze();
        this.level = answerWithDetailDTO.getLevel();
        this.metrics = answerWithDetailDTO.getMetrics();
    }


}