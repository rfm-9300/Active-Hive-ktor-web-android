package rfm.biblequizz.data.local

import rfm.biblequizz.data.local.entity.QuestionEntity
import rfm.biblequizz.domain.model.Question

fun Question.toEntity(): QuestionEntity {
    return QuestionEntity(
        uuid = uuid,
        question = question,
        correctAnswer = correctAnswer,
        wrongAnswers = wrongAnswers
    )
}

fun QuestionEntity.toDomain(): Question {
    return Question(
        uuid = uuid,
        question = question,
        correctAnswer = correctAnswer,
        wrongAnswers = wrongAnswers
    )
}