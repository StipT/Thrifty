package hr.stipanic.tomislav.thrifty.repository.local_database.dao

import androidx.room.*
import hr.stipanic.tomislav.thrifty.data.model.User
import io.reactivex.Completable

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewUser(user: User)
}