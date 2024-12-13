package io.github.octcarp.sustech.cs209a.proj.database.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import io.github.octcarp.sustech.cs209a.proj.database.entity.AnswerPO
import org.apache.ibatis.annotations.Mapper

@Mapper
interface AnswerMapper : BaseMapper<AnswerPO> {


}