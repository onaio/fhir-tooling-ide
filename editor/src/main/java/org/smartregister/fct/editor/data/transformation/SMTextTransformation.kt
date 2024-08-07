package org.smartregister.fct.editor.data.transformation

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import org.smartregister.fct.editor.domain.transformation.TextTransformation
import org.smartregister.fct.engine.util.hexToColor

internal class SMTextTransformation(
    searchText: String,
    isDarkTheme: Boolean,
    colorScheme: ColorScheme
) : TextTransformation(searchText) {

    private var blueColor = "#86B1FF".hexToColor()
    private var greenColor = "#91BE61".hexToColor()
    private var yellowColor = "#DEA834".hexToColor()

    init {
        if (!isDarkTheme) {
            blueColor = "#0050A5".hexToColor()
            greenColor = "#457700".hexToColor()
            yellowColor = "#BB8800".hexToColor()
        }
    }

    private val baseColorStyle = SpanStyle(color = colorScheme.onBackground)
    private val blueColorStyle = SpanStyle(color = blueColor)
    private val greenColorStyle = SpanStyle(color = greenColor)
    private val yellowColorStyle = SpanStyle(color = yellowColor)
    private val greyColorStyle = SpanStyle(color = "#A0A0A0".hexToColor())

    private val groupTagRegex = Regex("group ")
    private val mapTagRegex = Regex("map ")
    private val usesTagRegex = Regex("uses ")
    private val ruleLabelRegex = Regex("(\"[a-zA-Z_]+\";)(\\s*\\n|\\n)")
    private val sourceInputRegex = Regex("\\b([a-zA-Z]+)(?=\\s*(->\\s*))")
    private val dataAssignRegex = Regex("\\b([a-zA-Z._]+)(?=\\s*(<|>|<=|>=|!|!=|=|as\\s*))")
    private val createOjbectRegex = Regex("(?<=\\s|\\n)create(?=\\((['\"]).*(['\"])\\)\\s+)")
    private val sourceAndTargetRegex =
        Regex("(?<=\\(|,|,\\s|,\\s{2}|,\\s{3})\\n*\\s*(source|target)(?=\\s+.)")
    private val thenTagRegex = Regex("\\s*then(?=\\s*.)")
    private val asKeywordRegex = Regex("(?<=as\\s|as\\s{2}|as\\s{3}|as\\s{4})\\w+")
    private val stringLiteralRegex = Regex("((?<!\\\\)['\"])((?:.(?!(?<!\\\\)\\1))*.?)\\1(?!;)")
    private val dotRegex = Regex("\\.")
    private val commentRegex = Regex("(?<!.)(\\s*/{2}.*)")

    override fun AnnotatedString.Builder.transform(text: String) {
        styleText(text, groupTagRegex, blueColorStyle)
        styleText(text, mapTagRegex, blueColorStyle)
        styleText(text, usesTagRegex, blueColorStyle)
        styleText(text, asKeywordRegex, yellowColorStyle)
        //styleText(transformText, ASSIGNING_REGEX, YELLOW_COLOR_STYLE)
        styleText(text, ruleLabelRegex, greyColorStyle)
        styleText(text, sourceInputRegex, blueColorStyle)
        styleText(text, thenTagRegex, blueColorStyle)
        styleText(text, createOjbectRegex, blueColorStyle)
        styleText(text, sourceAndTargetRegex, blueColorStyle)
        styleText(text, stringLiteralRegex, greenColorStyle)
        styleText(text, dotRegex, baseColorStyle)
        styleText(text, commentRegex, greyColorStyle)
    }
}