package hr.stipanic.tomislav.thrifty.repository.local_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.data.model.Message
import hr.stipanic.tomislav.thrifty.data.model.User
import hr.stipanic.tomislav.thrifty.repository.local_database.converters.TypeConverter
import hr.stipanic.tomislav.thrifty.repository.local_database.dao.ArticleDao
import hr.stipanic.tomislav.thrifty.repository.local_database.dao.UserDao

@Database(entities = [Article::class, User::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class ThriftyDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    abstract fun userDao(): UserDao
}