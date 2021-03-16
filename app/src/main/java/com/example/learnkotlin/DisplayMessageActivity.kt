package com.example.learnkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.learnkotlin.databinding.ActivityDisplayMessageBinding

class DisplayMessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayMessageBinding
    private val tag = "DisplayMessageActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_display_message)

        //val message = intent.getStringExtra(EXTRA_MESSAGE)?.toInt() ?: 0
    }
}