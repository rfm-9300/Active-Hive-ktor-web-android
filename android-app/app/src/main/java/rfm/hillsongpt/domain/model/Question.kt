package rfm.hillsongpt.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val uuid: String,
    val title: String,
    @SerialName(value = "correct_answer") val correctAnswer: String,
    @SerialName(value = "wrong_answers") val wrongAnswers: List<String>
) {

}