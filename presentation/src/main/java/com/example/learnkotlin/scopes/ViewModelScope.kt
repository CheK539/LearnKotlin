package com.example.learnkotlin.scopes

import javax.inject.Scope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ViewModelScope
