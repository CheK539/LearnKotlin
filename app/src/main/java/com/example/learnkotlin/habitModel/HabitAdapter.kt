package com.example.learnkotlin.habitModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnkotlin.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.habit_element.view.*

class HabitAdapter(private val habitElements: List<HabitElement>) :
    RecyclerView.Adapter<HabitAdapter.HabitHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HabitHolder(inflater.inflate(R.layout.habit_element, parent, false))
    }

    override fun onBindViewHolder(holder: HabitHolder, position: Int) =
        holder.bind(habitElements[position])

    override fun getItemCount(): Int = habitElements.size

    class HabitHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(habitElement: HabitElement) {
            containerView.titleField.text = habitElement.title
            containerView.descriptionField.text = habitElement.description
            containerView.priorityField.text = habitElement.priority
            containerView.typeField.text = habitElement.type
            containerView.periodicityField.text = habitElement.periodicity
            containerView.colorField.text = habitElement.color
        }
    }
}