package com.roomedia.dawn_down_alarm.presentation.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import com.roomedia.dawn_down_alarm.BuildConfig
import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.presentation.view.activity.AlarmActivity


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findPreference<EditTextPreference>("version")?.apply {
            text = BuildConfig.VERSION_NAME
            setOnBindEditTextListener(null)
            onPreferenceClickListener = null
            onPreferenceChangeListener = null
        }

        findPreference<Preference>("update")?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}"))
            startActivity(intent)
            true
        }

        findPreference<Preference>("feedback")?.setOnPreferenceClickListener {
            val manager = ReviewManagerFactory.create(requireContext())
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (!task.isSuccessful) return@addOnCompleteListener
                val reviewInfo: ReviewInfo = task.result
                val flow = manager.launchReviewFlow(requireActivity(), reviewInfo)
                flow.addOnCompleteListener {}
            }
            true
        }

        findPreference<Preference>("license")?.setOnPreferenceClickListener {
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.license))
            startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            true
        }

        findPreference<Preference>("license")?.setOnPreferenceClickListener {
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.license))
            startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            true
        }

        findPreference<Preference>("test")?.setOnPreferenceClickListener {
            val intent = Intent(context, AlarmActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                .putExtra("packageName", BuildConfig.APPLICATION_ID)
                .putExtra("notification", getString(R.string.test_notification))
            startActivity(intent)
            true
        }
    }
}