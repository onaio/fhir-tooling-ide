package org.smartregister.fct.logcat.domain.model

import androidx.compose.ui.graphics.Color
import org.smartregister.fct.logcat.utils.hexToColor

enum class LogLevel(val value: String, val color: Color) {

    VERBOSE("V", "#91BE61".hexToColor()),
    DEBUG("D", "#86B1FF".hexToColor()),
    INFO("I", "#91BE61".hexToColor()),
    WARNING("W", "#DEA834".hexToColor()),
    ERROR("E", "#EB5F00".hexToColor()),
    ASSERT("A", "#91BE61".hexToColor()),
}

