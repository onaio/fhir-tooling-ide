package org.smartregister.fct.configs.domain.model.view.property

import androidx.compose.ui.text.font.FontWeight
import kotlinx.serialization.Serializable
import org.smartregister.fct.configs.domain.model.ViewType
import org.smartregister.fct.configs.util.extension.interpolate


@Serializable
data class CompoundTextProperties(
    override val viewType: ViewType = ViewType.COMPOUND_TEXT,
    override val weight: Float? = null,
    override val backgroundColor: String? = null,
    override val padding: Int? = null,
    override val borderRadius: Int? = null,
    override val alignment: ViewAlignment? = null,
    override val fillMaxWidth: Boolean? = null,
    override val fillMaxHeight: Boolean? = null,
    override val clickable: String? = null,
    override val visible: String? = null,
    val primaryText: String? = null,
    val primaryTextColor: String? = null,
    val secondaryText: String? = null,
    val secondaryTextColor: String? = null,
    val separator: String? = null,
    val fontSize: Float? = null,
    val primaryTextBackgroundColor: String? = null,
    val secondaryTextBackgroundColor: String? = null,
    val primaryTextFontWeight: TextFontWeight? = null,
    val secondaryTextFontWeight: TextFontWeight? = null,
    val primaryTextActions: List<org.smartregister.fct.configs.domain.model.ActionConfig>? = null,
    val secondaryTextActions: List<org.smartregister.fct.configs.domain.model.ActionConfig>? = null,
    val maxLines: Int? = null,
    val colorOpacity: Float? = null,
    val textCase: TextCase? = null,
    val overflow: TextOverFlow? = null,
    val letterSpacing: Int? = null,
) : ViewProperties() {
    override fun interpolate(computedValuesMap: Map<String, Any>): CompoundTextProperties {
        return this.copy(
            backgroundColor = backgroundColor?.interpolate(computedValuesMap),
            visible = (visible ?: "true").interpolate(computedValuesMap),
            primaryText = primaryText?.interpolate(computedValuesMap),
            secondaryText = secondaryText?.interpolate(computedValuesMap),
            primaryTextColor = primaryTextColor?.interpolate(computedValuesMap),
            primaryTextBackgroundColor = primaryTextBackgroundColor?.interpolate(computedValuesMap),
            secondaryTextColor = secondaryTextColor?.interpolate(computedValuesMap),
            secondaryTextBackgroundColor = secondaryTextBackgroundColor?.interpolate(
                computedValuesMap
            ),
            separator = separator?.interpolate(computedValuesMap),
        )
    }
}

enum class TextFontWeight(val fontWeight: FontWeight) {
    THIN(FontWeight.Thin),
    BOLD(FontWeight.Bold),
    EXTRA_BOLD(FontWeight.ExtraBold),
    LIGHT(FontWeight.Light),
    MEDIUM(FontWeight.Medium),
    NORMAL(FontWeight.Normal),
    BLACK(FontWeight.Black),
    EXTRA_LIGHT(FontWeight.ExtraLight),
    SEMI_BOLD(FontWeight.SemiBold),
}

enum class TextCase {
    UPPER_CASE,
    LOWER_CASE,
    CAMEL_CASE,
    TITLE_CASE,
}

enum class TextOverFlow {
    CLIP,
    ELLIPSIS,
    VISIBLE,
}
