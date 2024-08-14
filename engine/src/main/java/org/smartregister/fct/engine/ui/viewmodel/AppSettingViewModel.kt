package org.smartregister.fct.engine.ui.viewmodel

import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.engine.domain.model.AppSetting
import org.smartregister.fct.engine.domain.usecase.GetAppSetting
import org.smartregister.fct.engine.domain.usecase.UpdateAppSetting

class AppSettingViewModel : KoinComponent{

    private val getAppSetting: GetAppSetting by inject()
    private val updateAppSetting: UpdateAppSetting by inject()

    var appSetting = AppSetting()
        private set


    fun setAndUpdate(appSetting: AppSetting) {
        this.appSetting = appSetting
        updateAppSetting(appSetting)
    }

    fun setAppSetting(appSetting: AppSetting) {
        this.appSetting = appSetting
    }

    fun getAppSettingFlow() : Flow<AppSetting> {
        return getAppSetting()
    }
}