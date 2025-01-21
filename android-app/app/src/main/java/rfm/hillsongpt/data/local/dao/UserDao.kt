package rfm.hillsongpt.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import rfm.hillsongpt.data.local.entity.UserEntity

@Dao
interface UserDao : RoomDao<UserEntity> {
    @Query("SELECT * FROM user")
    fun get(): UserEntity?

    @Query("SELECT * FROM user WHERE id = :id")
    fun getById(id: Int): UserEntity

}