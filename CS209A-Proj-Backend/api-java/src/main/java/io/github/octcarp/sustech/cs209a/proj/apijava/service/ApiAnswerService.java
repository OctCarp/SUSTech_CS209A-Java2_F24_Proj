package io.github.octcarp.sustech.cs209a.proj.apijava.service;

import io.github.octcarp.sustech.cs209a.proj.apijava.mapper.AnswerAnalysisMapper;
import io.github.octcarp.sustech.cs209a.proj.apijava.dto.compound.AnswerWithDetailDTO;
import io.github.octcarp.sustech.cs209a.proj.apijava.vo.response.compound.AnswerQualityVO;
import io.github.octcarp.sustech.cs209a.proj.database.entity.AnswerQualityPO;
import io.github.octcarp.sustech.cs209a.proj.database.service.AnswerQualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiAnswerService {
    @Autowired
    AnswerAnalysisMapper answerAnalysisMapper;

    @Autowired
    AnswerQualityService answerQualityService;

    public List<AnswerQualityVO> getAllAnswerQuality() {
        List<AnswerWithDetailDTO> answerQList = answerAnalysisMapper.findAllAnswersWithDetail();
        return answerQList.stream().map(AnswerQualityVO::new).toList();
    }

    public List<AnswerQualityPO> getAllStaticAnswerQuality() {
        return answerQualityService.list();
    }
}
