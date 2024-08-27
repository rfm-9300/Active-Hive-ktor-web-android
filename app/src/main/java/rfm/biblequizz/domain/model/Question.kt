package rfm.biblequizz.domain.model

data class Question(
    val id: Int,
    val question: String,
    val correctAnswer: String,
    val wrongAnswers: List<String>
) {

}