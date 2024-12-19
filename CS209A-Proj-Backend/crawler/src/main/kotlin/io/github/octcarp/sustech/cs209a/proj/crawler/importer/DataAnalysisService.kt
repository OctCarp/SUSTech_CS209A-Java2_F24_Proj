package io.github.octcarp.sustech.cs209a.proj.crawler.importer

import io.github.octcarp.sustech.cs209a.proj.crawler.model.AnswerWithDetailDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.model.toAnswerQualityPO
import io.github.octcarp.sustech.cs209a.proj.database.service.AnswerQualityService
import io.github.octcarp.sustech.cs209a.proj.database.service.AnswerService
import io.github.octcarp.sustech.cs209a.proj.database.service.QuestionService
import io.github.octcarp.sustech.cs209a.proj.database.service.UserService
import org.springframework.format.Printer
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class DataAnalysisService(
    private val answerQualityService: AnswerQualityService,
    private val answerService: AnswerService,
    private val questionService: QuestionService,
    private val userService: UserService
) {
    fun startAnalysis() {
        analyzeAnswerAndSave()
    }

//    private fun analyzeAndSaveQuestion() {
//        answerQualityService.selectAllAnswerWithDetail()
//            .parallelStream()
//            .map { answer ->
//                AnswerWithDetailDTO(answer).toAnswerQualityPO()
//            }
//            .collect(Collectors.toList())
//            .chunked(1000)
//            .forEach { batch ->
//                answerQualityService.saveOrUpdateBatch(batch)
//            }
//    }

    private fun analyzeAnswerAndSave() {
        answerService.list().parallelStream().forEach { answer ->
            val questionPO = questionService.getById(answer.questionId)
            val userPO = userService.getById(answer.ownerId)
            val answerQuality = AnswerWithDetailDTO(
                answer = answer,
                question = questionPO,
                owner = userPO
            ).toAnswerQualityPO()
            answerQualityService.saveOrUpdate(answerQuality)
        }
    }
}