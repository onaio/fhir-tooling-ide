package org.smartregister.fct.common.util

import androidx.compose.ui.graphics.Color

fun String.hexToColor(): Color {
    if (this[0] == '#') {
        var color = substring(1).toLong(16)
        if (length == 7) {
            color = color or 0x00000000ff000000L
        } else if (length != 9) {
            throw IllegalArgumentException("Unknown color")
        }
        return Color(color.toInt())
    }
    throw IllegalArgumentException("Unknown color")
}