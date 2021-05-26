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
        private val habitElementBinding: HabitElementBinding,
        private val onHabitListener: OnHabitListener,
        private val onCompleteButtonListener: OnCompleteButtonListener
    ) :
        RecyclerView.ViewHolder(habitElementBinding.root) {
        fun bind(habitElement: Habit) {
            habitElementBinding.titleField.text = habitElement.title
            habitElementBinding.descriptionField.text = habitElement.description
            habitElementBinding.priorityField.text = habitElement.priority.stringValue
            habitElementBinding.typeField.text = habitElement.type.typeString
            habitElementBinding.periodicityField.text = habitElement.periodNumber.toString()
            habitElementBinding.colorField.text = habitElement.color
            habitElementBinding.completeButton.setOnClickListener {
                onCompleteButtonListener.onCompleteButtonClick(
                    adapterPosition
                )
            }

            val color = try {
                Color.parseColor(habitElement.color.split(" ")[0])
            } catch (e: Exception) {
                Color.parseColor("#ffffff")
            }

            habitElementBinding.card.setBackgroundColor(color)
            habitElementBinding.card.setOnClickListener {
                onHabitListener.onHabitClick(
                    adapterPosition
                )
            }
        }
    }

    interface OnHabitListener {
        fun onHabitClick(position: Int)
    }

    interface OnCompleteButtonListener {
        fun onCompleteButtonClick(position: Int)
    }
}