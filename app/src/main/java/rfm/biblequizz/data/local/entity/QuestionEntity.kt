package rfm.biblequizz.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question")
data class QuestionEntity (
    @PrimaryKey val uuid: String,
    val question: String,
    @ColumnInfo(name = "correct_answer") val correctAnswer: String,
    @ColumnInfo(name = "wrong_answer") val wrongAnswers: List<String>,
    val level: Int? = null
)