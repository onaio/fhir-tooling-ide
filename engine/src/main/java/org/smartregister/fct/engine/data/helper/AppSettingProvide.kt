package org.smartregister.fct.engine.data.helper

import org.koin.core.component.KoinComponent
import org.smartregister.fct.database.Database
import org.smartregister.fct.engine.domain.usecase.GetAppSetting
import org.smartregister.fct.engine.domain.usecase.UpdateAppSetting

object AppSettingProvide : KoinComponent {

    private val dataSource = Database.getDatabase().appSettingsDaoQueries
    val getAppSetting: GetAppSetting = GetAppSetting(dataSource)
    val updateAppSetting: UpdateAppSetting = UpdateAppSetting(dataSource)
}