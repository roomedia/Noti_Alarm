package com.roomedia.dawn_down_alarm.presentation.view.fragment

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.roomedia.dawn_down_alarm.data.AppListViewModel
import com.roomedia.dawn_down_alarm.data.CommonViewModelFactory
import com.roomedia.dawn_down_alarm.databinding.FragmentListBinding
import com.roomedia.dawn_down_alarm.presentation.AlarmApplication
import com.roomedia.dawn_down_alarm.presentation.AppInstallBroadcastReceiver
import com.roomedia.dawn_down_alarm.presentation.view.adapter.AppListAdapter
import java.lang.ref.WeakReference

class AppListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val appListViewModel: AppListViewModel by viewModels {
        CommonViewModelFactory(AlarmApplication.instance.appDao)
    }

    private val appListAdapter by lazy { AppListAdapter(WeakReference(childFragmentManager)) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false).apply {
            appsRecyclerView.adapter = appListAdapter
        }
        subscribeViewModel()
        registerBroadcastReceiver()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeViewModel() {
        appListViewModel.dataset.observe({ lifecycle }) { appsWithKeywords ->
            appListAdapter.submitList(appsWithKeywords)
        }
    }

    private fun registerBroadcastReceiver() {
        val receiver = AppInstallBroadcastReceiver(WeakReference(appListViewModel))
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addDataScheme("package")
        }
        AlarmApplication.instance.registerReceiver(receiver, filter)
    }
}