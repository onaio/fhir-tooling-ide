package org.smartregister.fct.rules.domain.model

import kotlinx.serialization.Serializable

@Serializable
internal data class IntSize(
    val width: Int,
    val height: Int
) {

    override fun toString(): String = "$width x $height"

    companion object {
        /**
         * IntSize with a zero (0) width and height.
         */
        val Zero = IntSize(0, 0)
    }
}