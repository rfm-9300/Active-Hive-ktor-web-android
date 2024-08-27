package rfm.biblequizz.data.local

import rfm.biblequizz.data.local.entity.QuestionEntity
import rfm.biblequizz.domain.model.Question

fun Question.toEntity(): QuestionEntity {
    return QuestionEntity(
        id = id,
        question = question,
        correctAnswer = correctAnswer,
        wrongAnswers = wrongAnswers
    )
}