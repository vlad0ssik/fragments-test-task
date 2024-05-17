package com.example.fragmenttestcase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.example.fragmenttestcase.databinding.FragmentPageBinding

class PageFragment : Fragment() {

    private var position: Int? = null
    private lateinit var binding: FragmentPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(EXTRA_POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPageBinding.inflate(layoutInflater)
        if (position == 1) binding.minusButton.isGone = true
        setupClickListeners()
        binding.countOfPage.text = position.toString()
        return binding.root
    }

    private fun setupClickListeners() {
        with(binding) {
            addButton.setOnClickListener {
                (requireActivity() as MainActivity).add()
            }
            minusButton.setOnClickListener {
                (requireActivity() as MainActivity).delete()
            }
            createNotification.setOnClickListener {

                showNotification()
            }
        }
    }

    private fun showNotification() {
        val appContext = requireContext()
        if (ContextCompat.checkSelfPermission(
                appContext,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_RC
                )
            }
        } else {
            val notificationManager =
                appContext.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            val title = "You create a notification"
            val message = "Notification $position"
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
            val notification = NotificationCompat.Builder(appContext, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(
                    PendingIntent.getActivity(
                        appContext, 0, MainActivity.newInstance(appContext, position!!),
                        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                )
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()
            notificationManager.notify(position!!, notification)
        }
    }

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val CHANNEL_NAME = "CHANNEL_NAME"
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        private const val NOTIFICATION_PERMISSION_RC = 312

        @JvmStatic
        fun newInstance(pos: Int) =
            PageFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, pos)
                }
            }
    }
}