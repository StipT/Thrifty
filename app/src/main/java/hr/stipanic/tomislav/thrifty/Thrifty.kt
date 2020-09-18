package hr.stipanic.tomislav.thrifty

import android.app.Application
import hr.stipanic.tomislav.thrifty.di.repoModule
import hr.stipanic.tomislav.thrifty.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class Thrifty : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@Thrifty)
            modules(listOf(repoModule, viewModelModule ))
        }
    }
}

