package org.smartregister.fct.datatable.domain.feature

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.flow.StateFlow
import org.smartregister.fct.datatable.data.enums.OrderBy
import org.smartregister.fct.datatable.domain.model.DataRow

interface DTSortable {

    suspend fun applySort(dtColumn: DTColumn, orderBy: OrderBy) : Result<List<DataRow>>
}