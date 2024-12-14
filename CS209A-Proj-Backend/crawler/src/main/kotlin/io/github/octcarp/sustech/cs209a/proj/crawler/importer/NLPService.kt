package io.github.octcarp.sustech.cs209a.proj.crawler.importer

import edu.stanford.nlp.ling.CoreLabel
import edu.stanford.nlp.pipeline.CoreDocument
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import io.github.octcarp.sustech.cs209a.proj.common.KeywordPreset
import java.util.Properties

object NLPService {
    private val pipeline: StanfordCoreNLP

    private val topicMap = KeywordPreset.topicMap

    init {
        val props = Properties().apply {
            setProperty("annotators", "tokenize,ssplit,pos,lemma")
            setProperty("coref.algorithm", "neural")
        }
        pipeline = StanfordCoreNLP(props)
    }

    fun processText(text: String, filtration: Boolean = true): List<CoreLabel> {
        val document = CoreDocument(text)
        pipeline.annotate(document)

        return document.tokens().let { tokens ->
            if (filtration) {
                tokens.filter {
                    it.tag().run {
                        startsWith("NN") || startsWith("VB") || startsWith("JJ")
                    }
                }
            } else {
                tokens
            }
        }
    }

    fun countBugs(tokens: List<CoreLabel>): Map<String, Int> {
        return tokens
            .map { it.word() }
            .filter { word ->
                (word.endsWith("Exception") && word.length > 9) ||
                        (word.endsWith("Error") && word.length > 5)
            }
            .groupingBy { it }
            .eachCount()
    }

    fun calculateTopicScores(
        bodyTokens: List<CoreLabel>,
        tagTokens: List<CoreLabel>?,
        titleTokens: List<CoreLabel>?,
    ): String {
        val scores = topicMap.keys.associateWith { topic ->
            val topicWords = topicMap[topic] ?: setOf(topic)

            val bodyScore = calculateTokenScore(bodyTokens, topicWords)
            val titleScore = titleTokens?.let { calculateTokenScore(it, topicWords) } ?: bodyScore
            val tagScore = tagTokens?.let { calculateTokenScore(it, topicWords) } ?: bodyScore

            (tagScore * 10 + titleScore * 2 + bodyScore) / 13.0
        }


        val (bestTopic, bestScore) = scores.maxByOrNull { it.value } ?: return "other"

        return if (bestScore >= 1e-2) bestTopic else "other"
    }

    private fun calculateTokenScore(
        tokens: List<CoreLabel>,
        synonyms: Set<String>
    ): Double {
        val matches = tokens.count { token ->

            synonyms.any { synonym ->
                token.lemma().contains(synonym, ignoreCase = true)
                        || token.originalText().contains(synonym, ignoreCase = true)
//                        || synonym.contains(token.lemma(), ignoreCase = true)
//                        || synonym.contains(token.originalText(), ignoreCase = true)
            }
        }

        return matches.toDouble() / tokens.size
    }

//    @Deprecated("Do not use this method, count bug directly")
//    fun calculateBugFrequency(questionDTO: QuestionDTO): Double {
//        val titleTokens = processText(questionDTO.title)
//        val bodyTokens = processText(questionDTO.body)
//
//        val exceptionCount = bodyTokens.count { token ->
//            token.lemma().contains("exception", ignoreCase = true) ||
//                    token.originalText().contains("exception", ignoreCase = true)
//        }
//
//        val bugCount = bodyTokens.count { token ->
//            token.lemma().contains("bug", ignoreCase = true) ||
//                    token.originalText().contains("bug", ignoreCase = true)
//        }
//
//        return (exceptionCount + bugCount).toDouble() / bodyTokens.size
//    }
}