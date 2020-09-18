package hr.stipanic.tomislav.thrifty.data.model

import java.util.*

data class Message (
    var userId: String = "",
    var displayName: String = "",
    var content: String = "",
    var date: Long = Date().time
)