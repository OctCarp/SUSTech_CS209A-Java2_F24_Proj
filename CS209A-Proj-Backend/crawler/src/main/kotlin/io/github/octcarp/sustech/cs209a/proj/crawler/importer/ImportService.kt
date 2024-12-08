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
import io.github.octcarp.sustech.cs209a.proj.database.service.PostBugService
import io.github.octcarp.sustech.cs209a.proj.database.service.PostTopicService
import io.github.octcarp.sustech.cs209a.proj.database.service.QuestionService
import io.github.octcarp.sustech.cs209a.proj.database.service.TopicService
import io.github.octcarp.sustech.cs209a.proj.database.service.UserService
import jakarta.annotation.PostConstruct
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class StaticDataImportService(
    private val jdbcTemplate: JdbcTemplate,
    private val topicService: TopicService,
    private val postTopicService: PostTopicService,
    private val bugService: BugService,
    private val postBugService: PostBugService,

    private val questionService: QuestionService,
    private val answerService: AnswerService,
    private val commentService: CommentService,
    private val userService: UserService,
) {
    private val topicIdMap = (KeywordPreset.topicMap.keys + "unknown")
        .mapIndexed { index, topic -> topic to index.toLong() }.toMap()

    private val bugIdMap = mutableMapOf<String, Long>()
    private var bugMapId = 0L

    @PostConstruct
    fun importStaticDataFromFile() {
        jdbcTemplate.execute("SET session_replication_role = 'replica'")
        try {
            beforeImport()

            val questions = loadQuestionJson()
            val answers = loadAnswerJson()
            val comments = loadCommentJson()
            val users = loadUserJson()

            importQuestions(questions)
            importAnswers(answers)
            importComments(comments)
            importUsers(users)

            afterImport()
        } finally {
            jdbcTemplate.execute("SET session_replication_role = 'origin'")
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
        bugIdMap.map { (name, id) ->
            BugPO(
                bugId = id,
                bugName = name,
                bugType = if (name.endsWith("Exception")) BugType.EXCEPTION else BugType.ERROR,
                bugFrequency = 0,
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

        questions.parallelStream().forEach { question ->
            questionPOs.add(question.toPO())

            val titleToken = NLPService.processText(question.title)
            val bodyToken = NLPService.processText(question.body)

            val topic = NLPService.calculateTopicScores(titleToken, bodyToken)
            val topicId = topicIdMap[topic]!!

            postTopicPOs.add(
                PostTopicPO(
                    postId = question.questionId,
                    postType = PostType.QUESTION,
                    topicId = topicId
                )
            )

            NLPService.countBugs(bodyToken).forEach { (name, count) ->
                val bugId = bugIdMap.getOrPut(name) { ++bugMapId }
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
//        postBugService.batchSaveOrUpdate(postBugPOs)
        postTopicService.saveOrUpdateBatch(postTopicPOs)
    }

    private fun importAnswers(answers: List<AnswerDTO>) {
        answers.chunked(3000).forEach { chunk ->
            val answerPOs = mutableListOf<AnswerPO>()
            val postBugPOs = mutableListOf<PostBugPO>()

            chunk.forEach { answer ->
                answerPOs.add(answer.toPO())
                val bodyToken = NLPService.processText(answer.body)

                NLPService.countBugs(bodyToken).forEach { (name, count) ->
                    val bugId = bugIdMap.getOrPut(name) { ++bugMapId }
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
        comments.chunked(3000).forEach { chunk ->
            val commentPOs = mutableListOf<CommentPO>()
            val postBugPOs = mutableListOf<PostBugPO>()

            chunk.forEach { comment ->
                commentPOs.add(comment.toPO())
                val bodyToken = NLPService.processText(comment.body)

                NLPService.countBugs(bodyToken).forEach { (name, count) ->
                    val bugId = bugIdMap.getOrPut(name) { ++bugMapId }
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
        users.chunked(5000).forEach { chunk ->
            val userPOs = chunk.map { it.toPO() }
            userService.saveOrUpdateBatch(userPOs)
        }
    }

    @Deprecated("Use importQuestions instead")
    private fun importQuestionsBak(questions: List<QuestionDTO>) {
        questions.forEach {
            questionService.saveOrUpdate(it.toPO())
            val titleToken = NLPService.processText(it.title)
            val bodyToken = NLPService.processText(it.body)

            val topic = NLPService.calculateTopicScores(titleToken, bodyToken)
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