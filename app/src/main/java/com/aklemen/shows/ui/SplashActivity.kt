package com.aklemen.shows.ui

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.*
import androidx.appcompat.app.AppCompatActivity
import com.aklemen.shows.R
import com.aklemen.shows.ui.login.LoginActivity
import com.aklemen.shows.ui.shows.shared.ShowsMasterActivity
import com.aklemen.shows.util.ShowsApp
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {

    companion object {

        private const val START_DELAY = 2000L

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkLoginStatus()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {

            val scaleAnimation = ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)

            scaleAnimation.apply {
                duration = (START_DELAY / 10) * 3
                interpolator = OvershootInterpolator(2f)
            }

            val translateAnimation = TranslateAnimation(0f, 0f, (-splashRoot.height - splashImage.height).toFloat(), 0f)

            translateAnimation.apply {
                duration = (START_DELAY / 10) * 5
                interpolator = BounceInterpolator()
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        splashText.startAnimation(scaleAnimation)
                        splashText.visibility = View.VISIBLE
                    }

                    override fun onAnimationStart(animation: Animation?) {}

                })
            }

            splashImage.startAnimation(translateAnimation)
        }
    }

    private fun checkLoginStatus() {
        if (ShowsApp.getToken().isNotEmpty() && ShowsApp.getRememberMe()) {
            Handler().postDelayed({
                startActivity(ShowsMasterActivity.newStartIntent(this))
                finish()
            }, START_DELAY)
        } else {
            Handler().postDelayed({
                startActivity(LoginActivity.newStartIntent(this))
                finish()
            }, START_DELAY)

        }
    }
}
