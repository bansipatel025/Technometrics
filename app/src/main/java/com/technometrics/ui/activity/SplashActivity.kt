package com.technometrics.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.technometrics.R
import com.technometrics.base.BaseActivity
import com.technometrics.databinding.ActivitySplashBinding
import com.technometrics.ui.factory.SplashFactory
import com.technometrics.ui.viewmodel.splash.SplashViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SplashActivity : BaseActivity(), KodeinAware {
    private lateinit var viewmodel: SplashViewModel
    private val factory: SplashFactory by instance()
    override val kodein: Kodein by kodein()
    private lateinit var binding: ActivitySplashBinding
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = DataBindingUtil.setContentView(activity, R.layout.activity_splash)
        binding.tvAppVersion.setText(resources.getString(R.string.version) + " 1.1")
        setupViewModel()
        setCode()
    }

    //set view model
    private fun setupViewModel() {
        viewmodel = ViewModelProvider(this, factory).get(SplashViewModel::class.java)
        binding.viewmodel = viewmodel
    }

    override fun setCode() {
        handler = Handler(Looper.getMainLooper())
        handler!!.postDelayed({
            startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            finish()
        }, 3000)
    }

    override fun clickListener() {

    }

    override fun getIntentData() {

    }

}