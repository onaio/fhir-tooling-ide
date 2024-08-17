package org.smartregister.fct.common.domain.usecase

import org.smartregister.fct.common.domain.model.AppSetting
import org.smartregister.fct.common.util.encodeJson
import sqldelight.AppSettingsDaoQueries

class UpdateAppSetting(private val appSettingDao: AppSettingsDaoQueries) {

    operator fun invoke(appSetting: AppSetting) {
        appSettingDao.update(appSetting.encodeJson())
    }
}