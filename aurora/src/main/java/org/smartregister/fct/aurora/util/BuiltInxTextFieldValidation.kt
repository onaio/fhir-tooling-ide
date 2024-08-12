package org.smartregister.fct.aurora.util

import org.smartregister.fct.aurora.ui.components.dialog.TextFieldValidation


val newFolderNameValidation: TextFieldValidation = {
    val result = "[\\s*a-zA-Z0-9_-]+".toRegex().matches(it)
    if (!result) {
        Pair(false, "Only alphabets, numbers, dash, underscore and space are Allowed")
    } else {
        Pair(true, "")
    }
}