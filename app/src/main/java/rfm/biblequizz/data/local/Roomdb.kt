package rfm.biblequizz.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import rfm.biblequizz.data.local.dao.QuestionDao
import rfm.biblequizz.data.local.dao.UserDao
import rfm.biblequizz.data.local.entity.QuestionEntity
import rfm.biblequizz.data.local.entity.UserEntity
import rfm.biblequizz.data.local.typeConverters.Converters

@Database(
    version = 1,
    entities = [
        UserEntity::class,
        QuestionEntity::class
    ]
)
@TypeConverters(Converters::class)
abstract class Roomdb: RoomDatabase() {
    companion object{
        fun build(context: Context) = Room.databaseBuilder(
            context,
            Roomdb::class.java,
            "courier.db"
        ).build()
    }


    abstract val userDao: UserDao
    abstract val questionDao: QuestionDao

}