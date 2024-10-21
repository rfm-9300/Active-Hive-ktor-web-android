package rfm.hillsongpt.data.local

import rfm.hillsongpt.data.local.entity.QuestionEntity
import rfm.hillsongpt.domain.model.Question

fun Question.toEntity(): QuestionEntity {
    return QuestionEntity(
        uuid = uuid,
        question = title,
        correctAnswer = correctAnswer,
        wrongAnswers = wrongAnswers
    )
}

fun QuestionEntity.toDomain(): Question {
    return Question(
        uuid = uuid,
        title = question,
        correctAnswer = correctAnswer,
        wrongAnswers = wrongAnswers
    )
}