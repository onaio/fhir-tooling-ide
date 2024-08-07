package org.smartregister.fct.engine.data.viewmodel

import org.smartregister.fct.engine.data.helper.AppSettingProvide
import org.smartregister.fct.engine.domain.model.AppSetting

class AppSettingViewModel {

    var appSetting = AppSetting()

    fun update() {
        AppSettingProvide.updateAppSetting(appSetting)
    }
}