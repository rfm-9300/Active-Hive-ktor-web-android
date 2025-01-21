package rfm.hillsongpt.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

/**
 * Interface with the basic methods that the DAOs of the application must implement for
 * Insert, Update, and Delete. All remaining @Query("SELECT ...") statements should be
 * declared in the child interfaces.
 *
 * It is recommended to annotate with @Transaction all @Query methods that have a SELECT statement
 * in the following cases:
 *
 * 1. The result of the query is large.
 * 2. The result of the Query is a POJO with @Relation fields. This way, we ensure that
 * each independent @Query("SELECT ...") (for each @Relation) is executed within the same
 * transaction, and the results are consistent across queries.
 *
 * [T] Type of entity that the DAO will work with
 */
interface RoomDao<T> {

    /**
     * Stores the [entity] in the database and returns the rowId of the newly inserted entity.
     * If the entity is a "rowid table" (its PrimaryKey is an INTEGER), the rowId corresponds
     * with its assigned ID.
     */
    @Insert
    fun save(entity: T): Long

    /**
     * Same behavior as [save] except that it returns a list with the rowId of all
     * the new insertions.
     */
    @Insert
    fun save(entities: List<T>): List<Long>

    /**
     * Updates [entity] in the database and returns the number of affected records.
     */
    @Update
    fun update(entity: T): Int

    /**
     * Updates the list of [entities] in the database and returns the number of
     * affected records.
     */
    @Update
    fun update(entities: List<T>): Int

    /**
     * Deletes the [entity] from the database and returns the number of affected records.
     */
    @Delete
    fun delete(entity: T): Int

    /**
     * Deletes the list of [entities] from the database and returns the number of affected records.
     */
    @Delete
    fun delete(entities: List<T>): Int

}
