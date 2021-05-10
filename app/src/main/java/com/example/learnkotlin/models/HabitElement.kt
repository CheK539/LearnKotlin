package com.example.learnkotlin.models

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.learnkotlin.converters.HabitTypeConverter
import com.example.learnkotlin.converters.PriorityConverter
import com.example.learnkotlin.enums.HabitType
import com.example.learnkotlin.enums.PriorityType
import com.google.gson.*
import java.lang.reflect.Type

//ToDo: сделать periodNumber Int

@TypeConverters(PriorityConverter::class, HabitTypeConverter::class)
@Entity(tableName = "habit_table")
data class HabitElement(
    var title: String,
    var description: String,
    var priority: PriorityType,
    var type: HabitType,
    var completeCounter: Int,
    var periodNumber: String,
    var color: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var uid = ""
    var date = 0
}

data class HabitElementUid(
    val uid: String
)

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
            json.asJsonObject.get("frequency").asString,
            color
        )
    }
}

class HabitElementSerializer : JsonSerializer<HabitElement> {
    override fun serialize(
        src: HabitElement,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonObject().apply {
            addProperty("color", Color.parseColor(src.color.split(" ")[0]))
            addProperty("count", src.completeCounter)
            addProperty("date", ++src.date)
            addProperty("description", src.description)
            addProperty("frequency", src.periodNumber.toInt())
            addProperty("priority", src.priority.priorityId)
            addProperty("title", src.title)
            addProperty("type", src.type.idType)
            addProperty("uid", src.uid)
        }
    }
}



