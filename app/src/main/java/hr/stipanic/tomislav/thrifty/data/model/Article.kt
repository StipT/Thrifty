package hr.stipanic.tomislav.thrifty.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.*


@Entity
data class Article (
    @PrimaryKey
    var id: String = "0",
    var userId: String = "0",
    var title: String = "",
    var category: String = "",
    var desc: String = "",
    var price: Float = 0f,
    var country: String = "",
    var city: String = "",
    var lat: Float = 0f,
    var lng: Float = 0f,
    var imgSrc: String = "",
    var date: Long = 0,
    @Ignore
    var messages: List<Message> = listOf()
)





