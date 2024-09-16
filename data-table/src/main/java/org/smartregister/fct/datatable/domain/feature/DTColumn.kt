package org.smartregister.fct.datatable.domain.feature

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface DTColumn {
    val index: Int
    val name: String
    val sortable: Boolean
    val editable: Boolean
    val isPrimary: Boolean
}