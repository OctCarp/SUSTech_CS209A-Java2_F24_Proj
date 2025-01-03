package io.github.octcarp.sustech.cs209a.proj.apijava.controller;

import io.github.octcarp.sustech.cs209a.proj.apijava.service.ApiAnswerService;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound.AnswerQualityVO;
import io.github.octcarp.sustech.cs209a.proj.database.entity.AnswerQualityPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private ApiAnswerService apiAnswerService;

    @GetMapping("/all/quality/static")
    public List<AnswerQualityPO> getAllStaticAnswerQuality() {
        return apiAnswerService.getAllStaticAnswerQuality();
    }

    @Deprecated
    @GetMapping("/all/quality")
    public List<AnswerQualityVO> getAllAnswerQuality() {
        return apiAnswerService.getAllAnswerQuality();
    }

    @Deprecated
    @GetMapping("/id/{id}/quality")
    public AnswerQualityVO getAnswerQualityById(
        @PathVariable("id") Long answerId
    ) {
//        AnswerQualityVO answerQualityVO = apiAnswerService.getAnswerQualityById(answerId);
//        if (answerQualityVO == null) {
//            throw new ResourceNotFoundException("Answer not found");
//        }
//        return answerQualityVO;
        return null;
    }
}