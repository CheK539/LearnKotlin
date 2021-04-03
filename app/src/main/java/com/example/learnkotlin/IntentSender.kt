package com.example.learnkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.learnkotlin.models.HabitElement

object IntentSender {
    fun sendToDisplayFormActivity(
        activity: AppCompatActivity,
        position: Int = -1,
        habitElement: HabitElement?
    ) {
        val intent = Intent(activity, DisplayFormActivity::class.java)
        intent.putExtra(EXTRA_POSITION, position)
        if (habitElement != null)
            intent.putExtra(EXTRA_HABIT_ELEMENT, habitElement)
        startActivityForResult(activity, intent, 1, null)
    }
}