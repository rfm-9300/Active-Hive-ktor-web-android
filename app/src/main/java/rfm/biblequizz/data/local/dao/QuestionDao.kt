package rfm.biblequizz.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import rfm.biblequizz.data.local.entity.QuestionEntity

@Dao
interface QuestionDao: RoomDao<QuestionEntity> {

    @Query("SELECT * FROM question")
    fun get(): QuestionEntity

    @Query("SELECT * FROM question WHERE id = :id")
    fun getById(id: Int): QuestionEntity

    @Query("SELECT * FROM question WHERE level = :level")
    fun getByLevel(level: Int): QuestionEntity

}