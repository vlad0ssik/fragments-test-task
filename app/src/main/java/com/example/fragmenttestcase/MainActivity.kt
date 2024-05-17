package com.example.fragmenttestcase

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fragmenttestcase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PagerAdapter
    private val sharedPreferences by lazy {
        getSharedPreferences(
            SHARED_PREFERENCE_KEY,
            MODE_PRIVATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = PagerAdapter(this)
        binding.viewPager.adapter = adapter
        getExtras()

    }
    private fun getExtras() {
        if (intent.hasExtra(POSITION_EXTRA_CONST)) {
            val count = intent.getIntExtra(POSITION_EXTRA_CONST, 1)
            adapter.submitList(count)
            binding.viewPager.currentItem = count - 1
        }
        else{
            adapter.submitList(sharedPreferences.getInt(LIST_SIZE_PREFERENCE, 1))

        }
    }

    override fun onStop() {
        super.onStop()
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(LIST_SIZE_PREFERENCE, adapter.itemCount)
        editor.apply()
    }
    fun delete() {
        adapter.removeFragment()
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(adapter.itemCount + 1)
    }

    fun add() {
        adapter.addFragment()
    }
    companion object {
        private const val POSITION_EXTRA_CONST = "POSITION_EXTRA_CONST"
        private const val LIST_SIZE_PREFERENCE = "LIST_SIZE_PREFERENCE"
        private const val SHARED_PREFERENCE_KEY = "shared_preference"
        fun newInstance(context: Context, position: Int): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(POSITION_EXTRA_CONST, position)
            }
        }
    }

}