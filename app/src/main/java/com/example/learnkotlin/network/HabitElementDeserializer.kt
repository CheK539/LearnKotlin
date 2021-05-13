package com.example.learnkotlin.network

import android.graphics.Color
import com.example.learnkotlin.enums.HabitType
import com.example.learnkotlin.enums.PriorityType
import com.example.learnkotlin.models.HabitElement
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class HabitElementDeserializer : JsonDeserializer<HabitElement> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): HabitElement {
        val rgb = json.asJsonObject.get("color").asInt
        val hsv = FloatArray(3)
        Color.colorToHSV(rgb, hsv)
        val hexColor = "#${"%x".format(rgb)}"
        val color = "$hexColor ${hsv[0]} ${hsv[1]} ${hsv[2]}"

        return HabitElement(
            json.asJsonObject.get("title").asString,
            json.asJsonObject.get("description").asString,
            PriorityType.fromInt(json.asJsonObject.get("priority").asInt),
            HabitType.fromId(json.asJsonObject.get("type").asInt),
            json.asJsonObject.get("count").asInt,
            json.asJsonObject.get("frequency").asInt,
            color
        ).apply { uid = json.asJsonObject.get("uid").asString }
    }
}