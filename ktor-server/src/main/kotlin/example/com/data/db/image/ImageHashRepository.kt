package example.com.data.db.image

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

interface ImageHashRepository {
    fun findByHash(hash: Int): ImageHash?
    fun save(imagePath: String, hash: Int): ImageHash
    fun delete(imagePath: String)
}

class ImageHashRepositoryImpl : ImageHashRepository {
    override fun findByHash(hash: Int): ImageHash? = transaction {
        ImageHashTable.select { ImageHashTable.hash eq hash }
            .map { it.toImageHash() }
            .firstOrNull()
    }

    override fun save(imagePath: String, hash: Int): ImageHash = transaction {
        val id = ImageHashTable.insert {
            it[ImageHashTable.imagePath] = imagePath
            it[ImageHashTable.hash] = hash
        }[ImageHashTable.id].value

        ImageHash(id, imagePath, hash)
    }

    override fun delete(imagePath: String) = transaction {
        ImageHashTable.deleteWhere { ImageHashTable.imagePath eq imagePath }
    }
} 