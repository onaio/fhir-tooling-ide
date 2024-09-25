package org.smartregister.fct.rules.domain.model

import kotlinx.serialization.Serializable
import org.smartregister.fct.rules.data.enums.Placement

@Serializable
internal data class Widget<T : java.io.Serializable>(
    var body: T,
    var x: Float = 0f,
    var y: Float = 0f,
    var size: IntSize = IntSize.Zero,
    var placement: Placement = Placement.Left,

    @kotlinx.serialization.Transient
    var parents: List<Widget<out java.io.Serializable>> = listOf(),

    @kotlinx.serialization.Transient
    var warnings: List<String> = listOf()
) {

    fun updatePlacement(boardProperty: BoardProperty) {
        this.placement = if (x > boardProperty.center.x) {
            Placement.Right
        } else if ((x + size.width) < boardProperty.center.x) {
            Placement.Left
        } else {
            Placement.Mid
        }
    }
}
