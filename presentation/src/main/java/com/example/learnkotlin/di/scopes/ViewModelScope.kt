package com.example.learnkotlin.di.scopes

import javax.inject.Scope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ViewModelScope
