package com.roo_media_.noti_alarm.ui.alarm

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager.LayoutParams.*
import androidx.appcompat.app.AppCompatActivity
import com.roo_media_.noti_alarm.R
import kotlinx.android.synthetic.main.activity_alarm.*

class AlarmActivity : AppCompatActivity() {

    private var count = 5
    private val alarm: Alarm by lazy {
        Alarm(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(
                FLAG_DISMISS_KEYGUARD or
                FLAG_SHOW_WHEN_LOCKED or
                FLAG_TURN_SCREEN_ON
            )
        }
        window.attributes.screenBrightness = 1.0f

        alarm.turnOn()
        intent.extras!!.run {
            iconImageView.setImageDrawable(
                packageManager.getApplicationIcon(getString("packageName")!!)
            )
            nameTextView.text = getString("name")
            notificationTextView.text = getString("notification")
        }

        awakeButton.text = getString(R.string.alarm_description, count)
        awakeButton.setOnClickListener {
            if (count > 1) {
                count -= 1
                awakeButton.text = getString(R.string.alarm_description, count)
            } else {
                count = 0
                alarm.turnOff()
                finish()
            }
        }
    }

    override fun onBackPressed() = Unit

    override fun onDestroy() {
        super.onDestroy()
        alarm.turnOff()
    }
}