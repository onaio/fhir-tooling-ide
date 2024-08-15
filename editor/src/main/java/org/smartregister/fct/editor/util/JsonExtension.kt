package org.smartregister.fct.editor.util

import com.google.gson.FormattingStyle
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import org.smartregister.fct.engine.ui.viewmodel.AppSettingViewModel
import org.smartregister.fct.engine.util.getKoinInstance

internal fun String.prettyJson(): String {

    val appSetting = getKoinInstance<AppSettingViewModel>().appSetting

    val gson = GsonBuilder()
        .disableHtmlEscaping()
        .setFormattingStyle(
            FormattingStyle.PRETTY.withIndent(
                " ".repeat(
                    appSetting.codeEditorConfig.indent
                )
            )
        )
        .create()
    val jsonParser = JsonParser.parseString(this).asJsonObject
    return gson.toJson(jsonParser)
}