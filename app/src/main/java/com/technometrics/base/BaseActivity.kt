package com.technometrics.base

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    val activity: Activity
        get() = this

    abstract fun clickListener()
    abstract fun setCode()
    abstract fun getIntentData()

}