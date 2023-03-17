package com.technometrics.ui.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.technometrics.data.repository.AppRepository
import com.technometrics.ui.viewmodel.moviedetail.MovieDetailViewModel

@Suppress("UNCHECKED_CAST")
class MovieDetailFactory(
    private val application: Application,
    val appRepository: AppRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDetailViewModel(application,appRepository) as T
    }
}