package io.github.octcarp.sustech.cs209a.proj.database.dto

import io.github.octcarp.sustech.cs209a.proj.database.entity.AnswerPO
import io.github.octcarp.sustech.cs209a.proj.database.entity.QuestionPO
import io.github.octcarp.sustech.cs209a.proj.database.entity.UserPO

data class AnswerWithDetail(
    var answer: AnswerPO,
    var question: QuestionPO,
    var owner: UserPO? = null,
)