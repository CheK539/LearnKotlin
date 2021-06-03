package com.example.learnkotlin

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.core.app.launchActivity
import com.agoda.kakao.spinner.KSpinnerItem
import com.example.learnkotlin.screens.FormScreen
import com.example.learnkotlin.screens.MainScreen
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DisplayFormFragmentTest : TestCase() {
    private lateinit var formScreen: FormScreen
    private lateinit var mainScreen: MainScreen
    private lateinit var newTitle: String
    private lateinit var updatedTitle: String

    @Before
    fun setUp() {
        launchActivity<MainActivity>()
        mainScreen = MainScreen()
        formScreen = FormScreen()
        newTitle = "Test title"
        updatedTitle = "Test title update"
    }

    @Test
    fun displayFormFragmentTest_should_addHabit() {
        mainScreen {
            addButton {
                isVisible()
                isClickable()
                click()
            }
        }

        formScreen {
            titleEditText {
                isVisible()
                isClickable()
                replaceText(newTitle)
            }

            descriptionEditText {
                isVisible()
                isClickable()
                replaceText("Test description")
            }

            spinner {
                isVisible()
                isClickable()
                open()
                childAt<KSpinnerItem>(1) {
                    isVisible()
                    click()
                }
            }

            periodEditText {
                isVisible()
                isClickable()
                replaceText("7")
            }

            completeCounterEditText {
                isVisible()
                isClickable()
                replaceText("5")
            }

            radioNegative {
                isVisible()
                isClickable()
                click()
            }

            colorButton {
                isVisible()
                isClickable()
                click()
            }

            toolbarSave {
                isVisible()
                isClickable()
                click()
            }
        }
    }

    @Test
    fun displayFormFragmentTest_should_updateHabit() {
        mainScreen {
            viewPager {
                childAt<MainScreen.PagerItem>(1) {
                    isVisible()
                    click()

                    recycler {
                        scrollToEnd()
                        isVisible()
                        lastChild<MainScreen.RecycleItem> {
                            title.hasText(newTitle)
                            isVisible()
                            click()
                        }
                    }
                }
            }
        }

        formScreen {
            titleEditText {
                isVisible()
                isClickable()
                replaceText(updatedTitle)
            }

            toolbarSave {
                isVisible()
                isClickable()
                click()
            }
        }
    }

    @Test
    fun displayFormFragmentTest_should_delete() {
        mainScreen {
            viewPager {
                childAt<MainScreen.PagerItem>(1) {
                    isVisible()
                    click()

                    recycler {
                        scrollToEnd()
                        isVisible()
                        lastChild<MainScreen.RecycleItem> {
                            title.hasText(updatedTitle)
                            isVisible()
                            click()
                        }
                    }
                }
            }
        }

        formScreen {
            toolbarDelete {
                isVisible()
                isClickable()
                click()
            }
        }
    }

    @Test
    fun displayFormFragmentTest_should_stayForm() {
        mainScreen {
            addButton {
                isVisible()
                isClickable()
                click()
            }
        }

        formScreen {
            toolbarSave {
                isVisible()
                isClickable()
                click()
            }

            titleEditText {
                isVisible()
            }
        }
    }
}