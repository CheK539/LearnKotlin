package com.example.learnkotlin.screens

import android.view.View
import com.agoda.kakao.pager2.KViewPager2
import com.agoda.kakao.pager2.KViewPagerItem
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.example.learnkotlin.R
import org.hamcrest.Matcher

class MainScreen : Screen<MainScreen>() {
    val addButton = KButton {
        withId(R.id.addButton)
    }

    val viewPager = KViewPager2({ withId(R.id.viewPager) },
        itemTypeBuilder = { itemType(::PagerItem) })


    class PagerItem(parent: Matcher<View>) : KViewPagerItem<PagerItem>(parent) {
        val recycler = KRecyclerView(parent, { withId(R.id.recycleView) },
            itemTypeBuilder = { itemType(::RecycleItem) })
    }

    class RecycleItem(parent: Matcher<View>) : KRecyclerItem<RecycleItem>(parent) {
        val title = KTextView(parent) { withId(R.id.titleField) }
    }
}