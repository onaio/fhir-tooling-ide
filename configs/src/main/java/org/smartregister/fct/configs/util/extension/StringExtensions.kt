package org.smartregister.fct.configs.util.extension

import org.apache.commons.text.StringSubstitutor
import org.smartregister.fct.logger.FCTLogger
import java.util.regex.Pattern

const val DEFAULT_PLACEHOLDER_PREFIX = "@{"
const val DEFAULT_PLACEHOLDER_SUFFIX = "}"
const val BLACK_COLOR_HEX_CODE = "#000000"
const val TRUE = "true"

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