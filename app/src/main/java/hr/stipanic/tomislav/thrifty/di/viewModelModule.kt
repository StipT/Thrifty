package hr.stipanic.tomislav.thrifty.di

import hr.stipanic.tomislav.thrifty.viewmodels.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { RegisterViewModel(get()) }

    viewModel { LoginViewModel(get()) }

    viewModel { FeedListViewModel(get()) }

    viewModel { ProfileViewModel(get()) }

    viewModel { DetailsViewModel(get()) }

    viewModel { AddViewModel(get()) }

    viewModel { ChatViewModel(get()) }

    viewModel { EditViewModel(get()) }
}