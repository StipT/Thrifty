package hr.stipanic.tomislav.thrifty.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    var userId: String = "0",
    var email: String = "unknown@unknown.unknown",
    var nickname: String = "Unknown"
)