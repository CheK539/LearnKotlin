package com.example.learnkotlin.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnkotlin.R
import com.example.learnkotlin.models.HabitElement
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.habit_element.view.*

class HabitAdapter(
    private val habitElements: List<HabitElement>,
    private val onListener: OnHabitListener
) :
    RecyclerView.Adapter<HabitAdapter.HabitHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HabitHolder(inflater.inflate(R.layout.habit_element, parent, false), onListener)
    }

    override fun onBindViewHolder(holder: HabitHolder, position: Int) =
        holder.bind(habitElements[position])

    override fun getItemCount(): Int = habitElements.size

    class HabitHolder(
        override val containerView: View,
        private val onHabitListener: OnHabitListener
    ) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer, View.OnClickListener {
        fun bind(habitElement: HabitElement) {
            containerView.titleField.text = habitElement.title
            containerView.descriptionField.text = habitElement.description
            containerView.priorityField.text = habitElement.priority.stringValue
            containerView.typeField.text = habitElement.type.typeString
            containerView.periodicityField.text = habitElement.periodNumber
            containerView.colorField.text = habitElement.color
            containerView.card.setBackgroundColor(Color.parseColor(habitElement.color.split(" ")[0]))
            containerView.card.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onHabitListener.onHabitClick(adapterPosition)
        }
    }

    interface OnHabitListener {
        fun onHabitClick(position: Int)
    }
}