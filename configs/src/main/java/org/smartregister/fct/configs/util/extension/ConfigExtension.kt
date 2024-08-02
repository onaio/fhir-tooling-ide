package org.smartregister.fct.configs.util.extension

import org.smartregister.fct.configs.data.viewmodel.ConfigTabViewModelContainer
import org.smartregister.fct.configs.data.viewmodel.ConfigViewModel


fun getConfigTabViewModel(uuid: String): ConfigViewModel {
    return ConfigTabViewModelContainer.tabViewModels[uuid]!!
}