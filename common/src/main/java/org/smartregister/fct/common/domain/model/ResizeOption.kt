package org.smartregister.fct.common.domain.model

import androidx.constraintlayout.compose.platform.annotation.FloatRange

internal const val MIN_SIZE_RATIO = 0.01f
internal const val MAX_SIZE_RATIO = 0.99f

sealed class ResizeOption(
    val sizeRatio: Float,
    val minSizeRatio: Float,
    val maxSizeRatio: Float,
) {


    class Fixed(
        @FloatRange(from = MIN_SIZE_RATIO.toDouble(), to = MAX_SIZE_RATIO.toDouble())
        sizeRatio: Float = 0.5f
    ) : ResizeOption(
        sizeRatio = sizeRatio,
        minSizeRatio = sizeRatio,
        maxSizeRatio = sizeRatio
    )

    class Flexible(
        @FloatRange(from = MIN_SIZE_RATIO.toDouble(), to = MAX_SIZE_RATIO.toDouble())
        sizeRatio: Float = 0.5f,
        minSizeRatio: Float = MIN_SIZE_RATIO,
        maxSizeRatio: Float = MAX_SIZE_RATIO,
    ) : ResizeOption(
        sizeRatio = sizeRatio,
        minSizeRatio = minSizeRatio,
        maxSizeRatio = maxSizeRatio
    )
}