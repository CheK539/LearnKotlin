package com.example.learnkotlin.viewsChanger

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetCallback(private val viewPager: ViewPager2) :
    BottomSheetBehavior.BottomSheetCallback() {
    override fun onStateChanged(bottomSheet: View, newState: Int) {
        when (newState) {
            BottomSheetBehavior.STATE_COLLAPSED -> viewPager.setPadding(
                0,
                0,
                0,
                BottomSheetBehavior.from(bottomSheet).peekHeight
            )

            BottomSheetBehavior.STATE_HIDDEN -> viewPager.setPadding(0, 0, 0, 0)
            BottomSheetBehavior.STATE_DRAGGING -> {
            }
            BottomSheetBehavior.STATE_EXPANDED -> {
            }
            BottomSheetBehavior.STATE_HALF_EXPANDED -> {
            }
            BottomSheetBehavior.STATE_SETTLING -> {
            }
        }
    }

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
        viewPager.setPadding(0, 0, 0, (bottomSheet.height * slideOffset).toInt())
    }
}