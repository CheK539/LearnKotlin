package com.example.learnkotlin.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Habit
import com.example.learnkotlin.databinding.HabitElementBinding

class HabitAdapter(
    private val habitElements: List<Habit>,
    private val onListener: OnHabitListener,
    private val onCompleteButtonListener: OnCompleteButtonListener
) :
    RecyclerView.Adapter<HabitAdapter.HabitHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitHolder {
        return HabitHolder(
            HabitElementBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onListener,
            onCompleteButtonListener
        )
    }

    override fun onBindViewHolder(holder: HabitHolder, position: Int) =
        holder.bind(habitElements[position])

    override fun getItemCount(): Int = habitElements.size

    class HabitHolder(
        private val habitBinding: HabitElementBinding,
        private val onHabitListener: OnHabitListener,
        private val onCompleteButtonListener: OnCompleteButtonListener
    ) :
        RecyclerView.ViewHolder(habitBinding.root) {
        fun bind(habitElement: Habit) {
            habitBinding.titleField.text = habitElement.title
            habitBinding.descriptionField.text = habitElement.description
            habitBinding.priorityField.text = habitElement.priority.stringValue
            habitBinding.typeField.text = habitElement.type.typeString
            habitBinding.periodicityField.text = habitElement.periodNumber.toString()
            habitBinding.colorField.text = habitElement.color
            habitBinding.completeButton.setOnClickListener {
                onCompleteButtonListener.onCompleteButtonClick(adapterPosition)
            }
            habitBinding.card.setOnClickListener {
                onHabitListener.onHabitClick(adapterPosition)
            }

            val color = try {
                Color.parseColor(habitElement.color.split(" ")[0])
            } catch (e: Exception) {
                Color.parseColor("#ffffff")
            }
            habitBinding.card.setBackgroundColor(color)
        }
    }

    interface OnHabitListener {
        fun onHabitClick(position: Int)
    }

    interface OnCompleteButtonListener {
        fun onCompleteButtonClick(position: Int)
    }
}