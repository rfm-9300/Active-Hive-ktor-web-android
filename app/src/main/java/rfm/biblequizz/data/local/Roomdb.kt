package rfm.biblequizz.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import rfm.biblequizz.data.local.dao.UserDao
import rfm.biblequizz.data.local.entity.UserEntity

@Database(
    version = 1,
    entities = [UserEntity::class]
)
abstract class Roomdb: RoomDatabase() {
    companion object{
        fun build(context: Context) = Room.databaseBuilder(
            context,
            Roomdb::class.java,
            "courier.db"
        ).build()
    }


    abstract val userDao: UserDao

}