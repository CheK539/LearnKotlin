package com.example.learnkotlin.screens

import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.spinner.KSpinner
import com.agoda.kakao.spinner.KSpinnerItem
import com.agoda.kakao.text.KButton
import com.agoda.kakao.toolbar.KToolbar
import com.example.learnkotlin.R

class FormScreen : Screen<FormScreen>() {
    val titleEditText = KEditText {
        withId(R.id.titleEditText)
    }
    val descriptionEditText = KEditText {
        withId(R.id.descriptionEditText)
    }
    val radioNegative = KButton {
        withId(R.id.negativeType)
    }
    val radioPositive = KButton {
        withId(R.id.positiveType)
    }
    val toolbarSave = KButton {
        withId(R.id.action_save)
    }
    val toolbarDelete = KToolbar {
        withId(R.id.action_delete)
    }
    val periodEditText = KEditText {
        withId(R.id.periodNumber)
    }
    val completeCounterEditText = KEditText {
        withId(R.id.completeCounter)
    }
    val colorButton = KButton {
        withId(R.id.imageColor)
    }

    val spinner = KSpinner({ withId(R.id.prioritySpinner) },
        itemTypeBuilder = { itemType(::KSpinnerItem) })
}

