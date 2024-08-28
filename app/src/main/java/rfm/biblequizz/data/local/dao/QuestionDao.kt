package rfm.biblequizz.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import rfm.biblequizz.data.local.entity.QuestionEntity

@Dao
interface QuestionDao: RoomDao<QuestionEntity> {

    @Query("SELECT * FROM question")
    fun getAll(): List<QuestionEntity>

    @Query("SELECT * FROM question WHERE uuid = :uuid")
    fun getById(uuid: String): QuestionEntity

    @Query("SELECT * FROM question WHERE level = :level")
    fun getByLevel(level: Int): QuestionEntity

}