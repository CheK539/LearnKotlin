package com.example.learnkotlin.network

import android.graphics.Color
import com.example.learnkotlin.models.HabitElement
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class HabitElementSerializer : JsonSerializer<HabitElement> {
    override fun serialize(
        src: HabitElement,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonObject().apply {
            addProperty("color", Color.parseColor(src.color.split(" ")[0]))
            addProperty("count", src.completeCounter)
            addProperty("date", src.date)
            addProperty("description", src.description)
            addProperty("frequency", src.periodNumber)
            addProperty("priority", src.priority.priorityId)
            addProperty("title", src.title)
            addProperty("type", src.type.idType)
            addProperty("uid", src.uid)
        }
    }
}