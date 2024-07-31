package rfm.biblequizz.data.local.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Question: RealmObject {
    @PrimaryKey var id: ObjectId = ObjectId()
    var question: String = ""
    var option1: String = ""
    var option2: String = ""
    var option3: String = ""
    var option4: String = ""
    var answer: Int = 0
}