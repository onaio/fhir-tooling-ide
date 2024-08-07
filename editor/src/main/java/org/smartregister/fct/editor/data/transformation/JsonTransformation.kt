package org.smartregister.fct.editor.data.transformation

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import org.smartregister.fct.editor.domain.transformation.TextTransformation
import org.smartregister.fct.engine.util.hexToColor

internal class JsonTransformation(
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

    private val baseColor = SpanStyle(color = colorScheme.onBackground)
    private val blueColorStyle = SpanStyle(color = blueColor)
    private val greenColorStyle = SpanStyle(color = greenColor)
    private val yellowColorStyle = SpanStyle(color = yellowColor)

    private val openCurlyBracketRegex = Regex("(?<!\\w)\\{(?=\\s*\")")
    private val closeCurlyBracketRegex = Regex("\\}(?=\\s*,|\\n)")
    private val openBracketRegex = Regex("\\[(?=\\s*(\\{|\\[|\"|'|false|true|\\.?\\d))")
    private val closeBracketRegex = Regex("(?<=(false|true|\\n|\\s|\\}|]|\"|'|\\d))]")
    private val keyRegex = Regex("((?<!\\\\)['\"])((?:.(?!(?<!\\\\)\\1))*.?)\\1(?=\\s*\\n?\\r?:)")
    private val valueRegex =
        Regex("((?<!\\\\)['\"])((?:.(?!(?<!\\\\)\\1))*.?)\\1(?=\\s*(\\n?\\r?\\}|]|,))")
    private val valueRegexContinuation = Regex("(false|true|\\d*\\.?\\d)(?=\\s*\\n?\\r?([,}\\]]))")

    override fun AnnotatedString.Builder.transform(text: String) {
        styleText(text, openCurlyBracketRegex, yellowColorStyle)
        styleText(text, closeCurlyBracketRegex, yellowColorStyle)
        styleText(text, openBracketRegex, yellowColorStyle)
        styleText(text, closeBracketRegex, yellowColorStyle)
        styleText(text, keyRegex, blueColorStyle)
        styleText(text, valueRegex, greenColorStyle)
        styleText(text, valueRegexContinuation, greenColorStyle)
    }
}