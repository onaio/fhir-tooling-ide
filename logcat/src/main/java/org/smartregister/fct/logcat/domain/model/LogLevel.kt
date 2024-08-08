package org.smartregister.fct.logcat.domain.model

import androidx.compose.ui.graphics.Color

enum class LogLevel(val value: String, val color: Color) {

    VERBOSE("V", Color(0xFF91BE61)),
    DEBUG("D", Color(0xFF86B1FF)),
    INFO("I", Color(0xFF91BE61)),
    WARNING("W", Color(0xFFDEA834)),
    ERROR("E", Color(0xFFEB5F00)),
    ASSERT("A", Color(0xFF91BE61)),
}

