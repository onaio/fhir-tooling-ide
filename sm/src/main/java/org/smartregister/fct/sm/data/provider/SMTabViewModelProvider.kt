package org.smartregister.fct.sm.data.provider

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.smartregister.fct.sm.data.viewmodel.SMTabViewModel
import org.smartregister.fct.sm.domain.model.SMDetail

class SMTabViewModelProvider {

    private val activeSMTabViewModel = MutableStateFlow<SMTabViewModel?>(null)
    private val tabViewModels = mutableMapOf<String, SMTabViewModel>()

    fun addSMTabViewModel(smDetail: SMDetail) {
        if (!tabViewModels.containsKey(smDetail.id)) {
            tabViewModels[smDetail.id] = SMTabViewModel(smDetail)
        }
    }

    fun getSMTabViewModelsMap () : Map<String, SMTabViewModel> = tabViewModels

    fun removeSMTabViewModel(id: String) {
        tabViewModels.remove(id)
    }

    suspend fun updateActiveSMTabViewModel (id: String) {
        activeSMTabViewModel.emit(tabViewModels[id])
    }

    fun getActiveSMTabViewModel() : StateFlow<SMTabViewModel?> = activeSMTabViewModel
}