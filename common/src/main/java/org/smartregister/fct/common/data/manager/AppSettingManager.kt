package org.smartregister.fct.common.data.manager

import kotlinx.coroutines.flow.Flow
import org.smartregister.fct.common.domain.model.AppSetting
import org.smartregister.fct.common.domain.usecase.GetAppSetting
import org.smartregister.fct.common.domain.usecase.UpdateAppSetting

class AppSettingManager(
    private val getAppSetting: GetAppSetting,
    private val updateAppSetting: UpdateAppSetting
) {

    var appSetting = AppSetting()
        private set


    fun setAndUpdate(appSetting: AppSetting) {
        this.appSetting = appSetting
        updateAppSetting(appSetting)
    }

    fun setAppSetting(appSetting: AppSetting) {
        this.appSetting = appSetting
    }

    fun getAppSettingFlow(): Flow<AppSetting> {
        return getAppSetting()
    }
}