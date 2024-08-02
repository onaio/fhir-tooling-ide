package org.smartregister.fct.configs.util.extension

import org.apache.commons.text.CaseUtils
import org.apache.commons.text.StringSubstitutor
import org.smartregister.fct.logcat.FCTLogger
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.text.MessageFormat
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.regex.Pattern
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

const val DEFAULT_PLACEHOLDER_PREFIX = "@{"
const val DEFAULT_PLACEHOLDER_SUFFIX = "}"
const val BLACK_COLOR_HEX_CODE = "#000000"
const val TRUE = "true"

fun uuid(): String {
    return UUID.randomUUID().toString();
}

fun String.compress(): String {
    val byteStream = ByteArrayOutputStream()
    GZIPOutputStream(byteStream).bufferedWriter().use { it.write(this) }
    return Base64.getEncoder().encodeToString(byteStream.toByteArray())
}

fun String.decompress(): String {
    val compressedBytes = Base64.getDecoder().decode(this)
    val byteArrayInputStream = ByteArrayInputStream(compressedBytes)
    return GZIPInputStream(byteArrayInputStream).bufferedReader().use { it.readText() }
}

/**
 * Sample template string: { "saveFamilyButtonText" : {{ family.button.save }} } Sample properties
 * file content: family.button.save=Save Family
 *
 * @param lookupMap The Map with the key value items to be used for interpolation
 * @param prefix The prefix of the key variable to interpolate. In the above example it is {{.
 *   Default is @{
 * @param suffix The prefix of the key/variable to interpolate. In the above example it is }}.
 *   Default is }
 * @return String with the interpolated value. For the sample case above this would be: {
 *   "saveFamilyButtonText" : "Save Family" }
 */
fun String.interpolate(
    lookupMap: Map<String, Any>,
    prefix: String = DEFAULT_PLACEHOLDER_PREFIX,
    suffix: String = DEFAULT_PLACEHOLDER_SUFFIX,
): String =
    try {
        StringSubstitutor.replace(
            this.replace(Pattern.quote(prefix).plus(".*?").plus(Pattern.quote(suffix)).toRegex()) {
                it.value.replace("\\s+".toRegex(), "")
            },
            lookupMap,
            prefix,
            suffix,
        )
    } catch (e: IllegalStateException) {
        FCTLogger.e(e)
        this
    }

/**
 * Wrapper method around the Java text formatter
 *
 * Example string format: Name {0} {1}, Age {2}
 *
 * @param locale this is the Locale to use e.g. Locale.ENGLISH
 * @param arguments this is a variable number of values to replace placeholders in order
 * @return the interpolated string with the placeholder variables replaced with the arguments
 *   values.
 *
 * In the example above, the result for passing arguments John, Doe, 35 would be: Name John Doe, Age
 * 35
 */
fun String.messageFormat(locale: Locale?, vararg arguments: Any?): String? =
    MessageFormat(this, locale).format(arguments)

/**
 * Creates identifier from string text by doing clean up on the passed value
 *
 * @return string.properties key to be used in string look ups
 */
fun String.translationPropertyKey(): String {
    return this.trim { it <= ' ' }.lowercase(Locale.ENGLISH).replace(" ".toRegex(), ".")
}

/**
 * This property returns the substring of the filepath after the last period '.' which is the
 * extension
 *
 * e.g /file/path/to/strings.txt would return txt
 */
val String.fileExtension
    get() = this.substringAfterLast('.')

/** Function that converts snake_case string to camelCase */
fun String.camelCase(): String = CaseUtils.toCamelCase(this, false, '_')

/**
 * Get the practitioner endpoint url and append the keycloak-uuid. The original String is assumed to
 * be a keycloak-uuid.
 */
fun String.practitionerEndpointUrl(): String = "PractitionerDetail?keycloak-uuid=$this"

/** Remove double white spaces from text and also remove space before comma */
fun String.removeExtraWhiteSpaces(): String =
    this.replace("\\s+".toRegex(), " ").replace(" ,", ",").trim()

/** Return an abbreviation for the provided string */
fun String?.abbreviate() = this?.firstOrNull() ?: ""

fun String.parseDate(pattern: String): Date? =
    SimpleDateFormat(pattern, Locale.ENGLISH).tryParse(this)

/** Compare characters of identical strings */
fun String.compare(anotherString: String): Boolean =
    this.toSortedSet().containsAll(anotherString.toSortedSet())

//fun String.lastOffset() = this.uppercase() + "_" + SharedPreferenceKey.LAST_OFFSET.name

fun String.spaceByUppercase() =
    this.split(Regex("(?=\\p{Upper})")).joinToString(separator = " ").trim()
