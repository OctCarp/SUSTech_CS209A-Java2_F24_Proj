package io.github.octcarp.sustech.cs209a.proj.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import io.github.octcarp.sustech.cs209a.proj.database.dto.AnswerWithDetail
import io.github.octcarp.sustech.cs209a.proj.database.entity.AnswerPO
import io.github.octcarp.sustech.cs209a.proj.database.entity.AnswerQualityPO
import io.github.octcarp.sustech.cs209a.proj.database.entity.QuestionPO
import io.github.octcarp.sustech.cs209a.proj.database.entity.UserPO
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.One
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select

@Mapper
interface AnswerQualityMapper : BaseMapper<AnswerQualityPO> {

//    @Select("SELECT * FROM Answers")
//    @Results(
//        Result(property = "answer", column = "answer_id", javaType = AnswerPO::class,
//            one = One(select = "io.github.octcarp.sustech.cs209a.proj.database.mapper.AnswerMapper.selectById")),
//
//        Result(property = "question", column = "question_id", javaType = QuestionPO::class,
//            one = One(select = "io.github.octcarp.sustech.cs209a.proj.database.mapper.QuestionMapper.selectById")),
//
//        Result(property = "owner", column = "owner_id", javaType = UserPO::class,
//            one = One(select = "io.github.octcarp.sustech.cs209a.proj.database.mapper.UserMapper.selectById"))
//    )
    fun selectAllAnswerWithDetail(): List<AnswerWithDetail>
}
