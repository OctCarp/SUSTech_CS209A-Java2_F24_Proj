package io.github.octcarp.sustech.cs209a.proj.apijava.controller;

import io.github.octcarp.sustech.cs209a.proj.apijava.exception.ResourceNotFoundException;
import io.github.octcarp.sustech.cs209a.proj.apijava.service.ApiAnswerService;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound.AnswerQualityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private ApiAnswerService apiAnswerService;

    @GetMapping("/{answer_id}/quality")
    public AnswerQualityVO getAnswerQualityById(
        @PathVariable("answer_id") Long answerId
    ) {
        AnswerQualityVO answerQualityVO = apiAnswerService.getAnswerQualityById(answerId);
        if (answerQualityVO == null) {
            throw new ResourceNotFoundException("Answer not found");
        }
        return answerQualityVO;
    }
}