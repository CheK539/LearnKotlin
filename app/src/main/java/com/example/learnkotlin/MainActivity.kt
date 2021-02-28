package com.example.learnkotlin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.learnkotlin.databinding.ActivityMainBinding

const val EXTRA_MESSAGE = "com.example.learnkotlin.MESSAGE"

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        Log.d(tag, "onCreate")
    }

    fun sendMessage(view: View)
    {
        val editText = binding.counter
        val message = editText.text.toString()
        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    override fun onSaveInstanceState(outState: Bundle)
    {
        val number = binding.counter.text.toString().toInt()
        binding.counter.text = "${number + 1}"
        super.onSaveInstanceState(outState)
    }

    override fun onStart()
    {
        super.onStart()
        Log.d(tag, "onStart")
    }

    override fun onPause()
    {
        super.onPause()
        Log.d(tag, "onPause")
    }

    override fun onResume()
    {
        super.onResume()
        Log.d(tag, "onResume")
    }

    override fun onRestart()
    {
        super.onRestart()
        Log.d(tag, "onRestart")
    }

    override fun onStop()
    {
        super.onStop()
        Log.d(tag, "onStop")
    }

    override fun onDestroy()
    {
        super.onDestroy()
        Log.d(tag, "onDestroy")
    }
}