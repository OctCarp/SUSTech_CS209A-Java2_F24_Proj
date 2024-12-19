package io.github.octcarp.sustech.cs209a.proj.apijava.mapper;

import io.github.octcarp.sustech.cs209a.proj.apijava.dto.compound.AnswerWithDetailDTO;
import io.github.octcarp.sustech.cs209a.proj.database.entity.AnswerPO;
import io.github.octcarp.sustech.cs209a.proj.database.entity.QuestionPO;
import io.github.octcarp.sustech.cs209a.proj.database.entity.UserPO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AnswerAnalysisMapper {
//    @Select("SELECT * FROM Answers")
//    @Results({
//        @Result(property = "answer", column = "answer_id", javaType = AnswerPO.class,
//            one = @One(select = "io.github.octcarp.sustech.cs209a.proj.database.mapper.AnswerMapper.selectById")),
//
//        @Result(property = "question", column = "question_id", javaType = QuestionPO.class,
//            one = @One(select = "io.github.octcarp.sustech.cs209a.proj.database.mapper.QuestionMapper.selectById")),
//
//        @Result(property = "owner", column = "owner_id", javaType = UserPO.class,
//            one = @One(select = "io.github.octcarp.sustech.cs209a.proj.database.mapper.UserMapper.selectById"))
//    })
    List<AnswerWithDetailDTO> findAllAnswersWithDetail();
}
