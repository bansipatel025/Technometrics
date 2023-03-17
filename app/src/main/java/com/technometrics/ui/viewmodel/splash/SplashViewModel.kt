package com.technometrics.ui.viewmodel.splash

import android.app.Application
import androidx.lifecycle.ViewModel

class SplashViewModel(
    var application: Application
) : ViewModel() {
    var no_internet_connection:String=""
}