package io.github.octcarp.sustech.cs209a.proj.apijava.service;

import io.github.octcarp.sustech.cs209a.proj.database.entity.AnswerPO;
import io.github.octcarp.sustech.cs209a.proj.database.entity.QuestionPO;
import io.github.octcarp.sustech.cs209a.proj.database.service.AnswerService;
import io.github.octcarp.sustech.cs209a.proj.database.service.QuestionService;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound.AnswerQualityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiAnswerService {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    public AnswerQualityVO getAnswerQualityById(Long answerId){
        AnswerPO answerPO = answerService.getBaseMapper().selectById(answerId);
        if (answerPO == null) {
            return null;
        }
        QuestionPO questionPO = questionService.getBaseMapper().selectById(answerPO.getQuestionId());

        return new AnswerQualityVO(questionPO, answerPO);
    }
}
