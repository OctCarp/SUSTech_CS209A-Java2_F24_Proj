package io.github.octcarp.sustech.cs209a.proj.crawler.importer

import io.github.octcarp.sustech.cs209a.proj.common.KeywordPreset
import io.github.octcarp.sustech.cs209a.proj.crawler.model.AnswerDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.model.CommentDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.model.QuestionDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.model.UserDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.model.toPO
import io.github.octcarp.sustech.cs209a.proj.crawler.utils.loadAnswerJson
import io.github.octcarp.sustech.cs209a.proj.crawler.utils.loadCommentJson
import io.github.octcarp.sustech.cs209a.proj.crawler.utils.loadQuestionJson
import io.github.octcarp.sustech.cs209a.proj.crawler.utils.loadUserJson
import io.github.octcarp.sustech.cs209a.proj.database.entity.AnswerPO
import io.github.octcarp.sustech.cs209a.proj.database.entity.BugPO
import io.github.octcarp.sustech.cs209a.proj.database.entity.CommentPO
import io.github.octcarp.sustech.cs209a.proj.database.entity.PostBugPO
import io.github.octcarp.sustech.cs209a.proj.database.entity.PostTopicPO
import io.github.octcarp.sustech.cs209a.proj.database.entity.QuestionPO
import io.github.octcarp.sustech.cs209a.proj.database.entity.TopicPO
import io.github.octcarp.sustech.cs209a.proj.database.enums.BugType
import io.github.octcarp.sustech.cs209a.proj.database.enums.PostType
import io.github.octcarp.sustech.cs209a.proj.database.service.AnswerService
import io.github.octcarp.sustech.cs209a.proj.database.service.BugService
import io.github.octcarp.sustech.cs209a.proj.database.service.CommentService
import io.github.octcarp.sustech.cs209a.proj.database.service.CommonSqlService
import io.github.octcarp.sustech.cs209a.proj.database.service.PostBugService
import io.github.octcarp.sustech.cs209a.proj.database.service.PostTopicService
import io.github.octcarp.sustech.cs209a.proj.database.service.QuestionService
import io.github.octcarp.sustech.cs209a.proj.database.service.TopicService
import io.github.octcarp.sustech.cs209a.proj.database.service.UserService
import org.mybatis.spring.annotation.MapperScan
import org.springframework.stereotype.Service

@Service
@MapperScan("io.github.octcarp.sustech.cs209a.proj.database.mapper")
class DataImportService(
    private val commonSqlService: CommonSqlService,
    private val topicService: TopicService,
    private val postTopicService: PostTopicService,
    private val bugService: BugService,
    private val postBugService: PostBugService,

    private val questionService: QuestionService,
    private val answerService: AnswerService,
    private val commentService: CommentService,
    private val userService: UserService,
) {
    private val topicIdMap = (KeywordPreset.topicMap.keys + "others")
        .mapIndexed { index, topic -> topic to index.toLong() }.toMap()
    private val topicFreqCount = mutableMapOf<Long, Long>()

    private val bugIdMap = mutableMapOf<String, Long>()
    val bugFreqCount = mutableMapOf<Long, Long>()
    private var bugMapId = 0L

    fun importStaticDataFromFile() {
        commonSqlService.disableAllTriggers()
        try {
            beforeImport()

            val questions = loadQuestionJson()
            val answers = loadAnswerJson()
            val comments = loadCommentJson()

            importQuestions(questions)
            importAnswers(answers)
            importComments(comments)

            val users = loadUserJson()
            importUsers(users)

            afterImport()
        } finally {
            commonSqlService.enableAllTriggers()
        }
    }

    private fun beforeImport() {
        topicIdMap.map { (topic, id) ->
            TopicPO(
                topicId = id,
                topicName = topic
            )
        }.let { topicPOs ->
            topicService.saveOrUpdateBatch(topicPOs)
        }
    }

    private fun afterImport() {
        topicIdMap.map { (topic, id) ->
            TopicPO(
                topicId = id,
                topicName = topic,
                frequency = topicFreqCount[id] ?: 0
            )
        }.let { topicPOs ->
            topicService.updateBatchById(topicPOs)
        }

        bugIdMap.map { (name, id) ->
            BugPO(
                bugId = id,
                bugName = name,
                bugType = if (name.endsWith("Error")) BugType.ERROR else BugType.EXCEPTION,
                bugFrequency = bugFreqCount[id] ?: 0,
                bugDesc = null
            )
        }.let { bugPOs ->
            bugService.saveOrUpdateBatch(bugPOs)
        }
    }

    private fun importQuestions(questions: List<QuestionDTO>) {
        val questionPOs = mutableListOf<QuestionPO>()
        val postBugPOs = mutableListOf<PostBugPO>()
        val postTopicPOs = mutableListOf<PostTopicPO>()

        questions.forEach { question ->
            questionPOs.add(question.toPO())

            val tagToken = NLPService.processText(question.tags.joinToString(","))
            val titleToken = NLPService.processText(question.title)
            val bodyToken = NLPService.processText(question.body)

            val topic = NLPService.calculateTopicScores(bodyToken, tagToken, titleToken)

            val topicId = topicIdMap[topic]!!
            topicFreqCount[topicId] = topicFreqCount.getOrDefault(topicId, 0) + 1

            postTopicPOs.add(
                PostTopicPO(
                    postId = question.questionId,
                    postType = PostType.QUESTION,
                    topicId = topicId
                )
            )

            NLPService.countBugs(bodyToken).forEach { (name, count) ->
                val bugId = bugIdMap.getOrPut(name) { ++bugMapId }
                bugFreqCount[bugId] = bugFreqCount.getOrDefault(bugId, 0) + count * 3

                postBugPOs.add(
                    PostBugPO(
                        postId = question.questionId,
                        postType = PostType.QUESTION,
                        bugId = bugId,
                        frequency = count
                    )
                )
            }
        }
        questionService.saveOrUpdateBatch(questionPOs)
        postBugService.saveOrUpdateBatch(postBugPOs)
        postTopicService.saveOrUpdateBatch(postTopicPOs)
    }

    private fun importAnswers(answers: List<AnswerDTO>) {
        answers.chunked(1000).forEach { chunk ->
            val answerPOs = mutableListOf<AnswerPO>()
            val postBugPOs = mutableListOf<PostBugPO>()

            chunk.forEach { answer ->
                answerPOs.add(answer.toPO())
                val bodyToken = NLPService.processText(answer.body)

                NLPService.countBugs(bodyToken).forEach { (name, count) ->
                    val bugId = bugIdMap.getOrPut(name) { ++bugMapId }
                    bugFreqCount[bugId] = bugFreqCount.getOrDefault(bugId, 0) + count * 2

                    postBugPOs.add(
                        PostBugPO(
                            postId = answer.answerId,
                            postType = PostType.ANSWER,
                            bugId = bugId,
                            frequency = count
                        )
                    )
                }
            }

            answerService.saveOrUpdateBatch(answerPOs)
            postBugService.saveOrUpdateBatch(postBugPOs)
        }
    }

    private fun importComments(comments: List<CommentDTO>) {
        comments.chunked(1000).forEach { chunk ->
            val commentPOs = mutableListOf<CommentPO>()
            val postBugPOs = mutableListOf<PostBugPO>()

            chunk.forEach { comment ->
                commentPOs.add(comment.toPO())
                val bodyToken = NLPService.processText(comment.body)

                NLPService.countBugs(bodyToken).forEach { (name, count) ->
                    val bugId = bugIdMap.getOrPut(name) { ++bugMapId }
                    bugFreqCount[bugId] = bugFreqCount.getOrDefault(bugId, 0) + count * 1

                    postBugPOs.add(
                        PostBugPO(
                            postId = comment.commentId,
                            postType = PostType.COMMENT,
                            bugId = bugId,
                            frequency = count
                        )
                    )
                }
            }

            commentService.saveOrUpdateBatch(commentPOs)
            postBugService.saveOrUpdateBatch(postBugPOs)
        }
    }

    private fun importUsers(users: List<UserDTO>) {
        users.chunked(1000).forEach { chunk ->
            val userPOs = chunk.map { it.toPO() }
            userService.saveOrUpdateBatch(userPOs)
        }
    }

    @Deprecated("Use importQuestions instead")
    private fun importQuestionsBak(questions: List<QuestionDTO>) {
        questions.forEach {
            questionService.saveOrUpdate(it.toPO())
            val tagToken = NLPService.processText(it.tags.joinToString(","))
            val titleToken = NLPService.processText(it.title)
            val bodyToken = NLPService.processText(it.body)

            val topic = NLPService.calculateTopicScores(bodyToken, tagToken, titleToken)
            val topicId = topicIdMap[topic]!!

            val bugCount: Map<String, Int> = NLPService.countBugs(bodyToken)

            bugCount.forEach { name, count ->
                {
                    val bugId = bugIdMap.getOrPut(name) {
                        ++bugMapId
                    }

                    postBugService.saveOrUpdate(
                        PostBugPO(
                            postId = it.questionId,
                            postType = PostType.QUESTION,
                            bugId = bugId,
                            frequency = count,
                        )
                    )
                }
            }

            postTopicService.saveOrUpdate(
                PostTopicPO(
                    postId = it.questionId,
                    postType = PostType.QUESTION,
                    topicId = topicId,
                )
            )
        }
    }
}