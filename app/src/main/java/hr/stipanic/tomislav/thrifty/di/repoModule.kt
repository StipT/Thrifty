package hr.stipanic.tomislav.thrifty.di

import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import hr.stipanic.tomislav.thrifty.repository.Repository
import hr.stipanic.tomislav.thrifty.repository.RepositoryImpl
import hr.stipanic.tomislav.thrifty.repository.local_database.RoomRepository
import hr.stipanic.tomislav.thrifty.repository.local_database.RoomRepositoryImpl
import hr.stipanic.tomislav.thrifty.repository.local_database.ThriftyDatabase
import hr.stipanic.tomislav.thrifty.repository.remote_data_source.auth.AuthSource
import hr.stipanic.tomislav.thrifty.repository.remote_data_source.auth.AuthSourceImpl
import hr.stipanic.tomislav.thrifty.repository.remote_data_source.database.FirestoreSource
import hr.stipanic.tomislav.thrifty.repository.remote_data_source.database.FirestoreSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repoModule = module {

    single {
        Room.databaseBuilder(androidApplication(), ThriftyDatabase::class.java, "ThriftyDatabase").build()
    }

    single { FirebaseAuth.getInstance() }

    single { FirebaseFirestore.getInstance() }

    single { FirebaseStorage.getInstance() }

    single<AuthSource> { AuthSourceImpl(get(), get()) }

    single<FirestoreSource> { FirestoreSourceImpl(get(), get(), get()) }

    single<RoomRepository> { RoomRepositoryImpl(get()) }

    single<Repository> { RepositoryImpl(get(), get()) }

}