package org.smartregister.fct.configs.data.viewmodel

object ConfigTabViewModelContainer {
    lateinit var activeViewModel: ConfigViewModel
    val tabViewModels = mutableMapOf<String, ConfigViewModel>()

    fun<T : ConfigViewModel> get() : T{
        return activeViewModel as T
    }
}
