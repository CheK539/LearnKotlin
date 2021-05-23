package com.example.learnkotlin.modules

import com.example.learnkotlin.components.HabitsComponent
import dagger.Module


@Module(subcomponents = [HabitsComponent::class])
class SubcomponentsModule