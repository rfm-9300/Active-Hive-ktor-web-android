package rfm.hillsongpt.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: Int,
    val username: String,
    val token: String,

    // nullable fields
    val email: String? = null,
    @ColumnInfo(name = "first_name") val firstName: String? = null,
    @ColumnInfo(name = "last_name") val lastName: String? = null,

){
    /**
     * Constructor to facilitate the creation of entities that are not yet stored in the database.
     */
    constructor(
        username: String,
        token: String,
    ) : this(0, username, token, null, null, null)
}