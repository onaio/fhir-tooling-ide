package org.smartregister.fct.engine.data.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.smartregister.fct.engine.data.helper.AppSettingProvide
import org.smartregister.fct.engine.domain.model.AppSetting

class AppSettingViewModel {

    var appSetting = AppSetting()

    fun update() {
        AppSettingProvide.updateAppSetting(appSetting)
    }
}