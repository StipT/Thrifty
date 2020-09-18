package hr.stipanic.tomislav.thrifty.repository.local_database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import hr.stipanic.tomislav.thrifty.data.model.Message
import java.util.*


class TypeConverter {
    companion object {

    //for List<Message>
    @TypeConverter
    @JvmStatic
    fun listToJson(value: List<Message>?): String = Gson().toJson(value)

    @TypeConverter
    @JvmStatic
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Message>::class.java).toList()

    /*
    //for Date
    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    @JvmStatic
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
     */
    }
}