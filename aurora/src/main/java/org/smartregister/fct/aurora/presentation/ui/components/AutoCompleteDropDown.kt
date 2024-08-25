package org.smartregister.fct.aurora.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.launch
import org.smartregister.fct.aurora.util.pxToDp

@Composable
fun <T> AutoCompleteDropDown(
    modifier: Modifier = Modifier,
    items: List<T>,
    label: (T) -> String,
    heading: String,
    defaultSelectedIndex: Int? = null,
    defaultValue: String? = null,
    key: Any? = null,
    onSelected: (T.(Int) -> Unit)? = null,
    onTextChanged: ((String, Boolean) -> Unit)? = null
) {

    require(defaultSelectedIndex == null || defaultValue == null) {
        throw IllegalStateException("{defaultSelectedIndex} and {defaultValue} cannot be provided both at a time provide only one of them")
    }

    if (defaultSelectedIndex != null) {
        require(defaultSelectedIndex in 0..items.size.minus(1)) {
            throw IndexOutOfBoundsException("{defaultSelectedIndex} should be greater than or equal to 0 and less than to {items}")
        }
    }

    val scope = rememberCoroutineScope()
    var searchText by remember(key) { mutableStateOf(TextFieldValue(defaultValue ?: "")) }
    var isError by remember(key) { mutableStateOf(false) }
    var expanded by remember(key) { mutableStateOf(false) }
    var textFieldWidth by remember(key) { mutableStateOf(0f) }
    val callOnInitialSelection = remember(key) { mutableStateOf(false) }

    val filteredItems = items.filter {
        label(it).contains(searchText.text, ignoreCase = true)
    }

    if (filteredItems.isEmpty()) {
        expanded = false
    }

    if (!callOnInitialSelection.value) {
        callOnInitialSelection.value = true

        if (defaultSelectedIndex != null) {
            val item = items[defaultSelectedIndex]
            searchText = TextFieldValue(label(item))
        }

        if (defaultValue != null && defaultValue.trim().isNotEmpty()) {
            isError = !items.any { label(it) == defaultValue }
        }
    }

    Column {
        OutlinedTextField(
            modifier = modifier
                .onGloballyPositioned { layoutCoordinates ->
                    textFieldWidth = layoutCoordinates.size.width.toFloat()
                },
            value = searchText,
            onValueChange = {

                searchText = it
                expanded = filteredItems.isNotEmpty()

                scope.launch {
                    val isMatch = items.any { item ->
                        searchText.text == label(item)
                    }

                    isError = searchText.text.trim().isNotEmpty() && !isMatch
                    onTextChanged?.invoke(searchText.text, isMatch)
                }

            },
            label = heading,
            isError = isError
        )

        DropdownMenu(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .width(textFieldWidth.pxToDp())
                .requiredSizeIn(maxHeight = 400.dp),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            properties = PopupProperties(
                focusable = false
            )
        ) {

            filteredItems.forEachIndexed { index, item ->
                DropdownMenuItem(
                    modifier = Modifier.height(40.dp),
                    text = {
                        Text(
                            text = label(item),
                            style = MaterialTheme.typography.titleSmall
                        )
                    },
                    onClick = {
                        searchText = TextFieldValue(
                            label(item),
                            selection = TextRange(label(item).length)
                        )
                        expanded = false
                        isError = false
                        onTextChanged?.invoke(label(item), true)
                        onSelected?.invoke(item, index)
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                )
            }
        }
    }
}