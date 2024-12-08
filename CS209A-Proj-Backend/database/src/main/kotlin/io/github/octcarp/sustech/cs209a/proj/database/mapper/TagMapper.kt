package io.github.octcarp.sustech.cs209a.proj.database.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import io.github.octcarp.sustech.cs209a.proj.database.entity.TagPO
import org.apache.ibatis.annotations.Mapper

@Mapper
interface TagMapper : BaseMapper<TagPO> {
}