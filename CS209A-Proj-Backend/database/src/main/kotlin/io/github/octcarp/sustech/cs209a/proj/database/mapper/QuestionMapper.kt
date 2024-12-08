package io.github.octcarp.sustech.cs209a.proj.database.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import io.github.octcarp.sustech.cs209a.proj.database.entity.QuestionPO
import org.apache.ibatis.annotations.Mapper

@Mapper
interface QuestionMapper : BaseMapper<QuestionPO> {
}