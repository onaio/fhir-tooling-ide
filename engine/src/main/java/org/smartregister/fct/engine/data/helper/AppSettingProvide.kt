package org.smartregister.fct.engine.data.helper

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.engine.domain.usecase.GetAppSetting
import org.smartregister.fct.engine.domain.usecase.UpdateAppSetting

object AppSettingProvide : KoinComponent {
    val getAppSetting: GetAppSetting by inject()
    val updateAppSetting: UpdateAppSetting by inject()
}