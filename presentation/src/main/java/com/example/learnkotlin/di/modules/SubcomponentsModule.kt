package com.example.learnkotlin.di.modules

import com.example.learnkotlin.di.components.HabitsComponent
import dagger.Module


@Module(subcomponents = [HabitsComponent::class])
class SubcomponentsModule