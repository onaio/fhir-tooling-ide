package org.smartregister.fct.sm.data.provider

import org.smartregister.fct.sm.data.viewmodel.SMTabViewModel

class SMTabViewModelProvider {
    private var activeTabIndex = 0
    val tabViewModels = mutableMapOf<String, SMTabViewModel>()

    fun updateActiveTabIndex(tabIndex: Int) {
        activeTabIndex = tabIndex
    }

    fun getActiveSMTabViewModel() : SMTabViewModel? {
        return tabViewModels
            .entries
            .mapIndexed { index, mutableEntry ->
                if (index == activeTabIndex) {
                    mutableEntry.value
                } else null
            }
            .firstOrNull()
    }
}