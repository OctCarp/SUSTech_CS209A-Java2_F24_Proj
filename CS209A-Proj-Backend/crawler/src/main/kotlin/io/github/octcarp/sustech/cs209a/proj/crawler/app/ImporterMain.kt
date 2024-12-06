package io.github.octcarp.sustech.cs209a.proj.crawler.app

import io.github.octcarp.sustech.cs209a.proj.crawler.utils.loadAnswerJson
import io.github.octcarp.sustech.cs209a.proj.crawler.utils.loadCommentJson
import io.github.octcarp.sustech.cs209a.proj.crawler.utils.loadQuestionJson
import io.github.octcarp.sustech.cs209a.proj.crawler.utils.loadUserJson


fun main(args: Array<String>) {
    ImporterMain.execute()
}

object ImporterMain {
    fun execute() {
        val questions = loadQuestionJson()
        val answers = loadAnswerJson()
        val comments = loadCommentJson()
        val users = loadUserJson()
    }
}