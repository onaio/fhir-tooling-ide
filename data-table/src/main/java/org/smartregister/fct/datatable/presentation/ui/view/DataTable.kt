package org.smartregister.fct.datatable.presentation.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.LastPage
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FirstPage
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.smartregister.fct.aurora.AuroraIconPack
import org.smartregister.fct.aurora.auroraiconpack.ExpandAll
import org.smartregister.fct.aurora.auroraiconpack.Sync
import org.smartregister.fct.aurora.presentation.ui.components.Icon
import org.smartregister.fct.aurora.presentation.ui.components.LinearIndicator
import org.smartregister.fct.aurora.presentation.ui.components.NumberDropDown
import org.smartregister.fct.aurora.presentation.ui.components.SmallIconButton
import org.smartregister.fct.aurora.presentation.ui.components.Tooltip
import org.smartregister.fct.aurora.presentation.ui.components.TooltipPosition
import org.smartregister.fct.common.presentation.ui.container.Aurora
import org.smartregister.fct.common.util.windowWidthResizePointer
import org.smartregister.fct.datatable.data.controller.DataTableController
import org.smartregister.fct.datatable.data.enums.OrderBy
import org.smartregister.fct.datatable.domain.feature.DTColumn
import org.smartregister.fct.datatable.domain.feature.DTCountable
import org.smartregister.fct.datatable.domain.feature.DTFilterColumn
import org.smartregister.fct.datatable.domain.feature.DTFilterable
import org.smartregister.fct.datatable.domain.feature.DTFilterableType
import org.smartregister.fct.datatable.domain.feature.DTPagination
import org.smartregister.fct.datatable.domain.feature.DTRefreshable
import org.smartregister.fct.datatable.domain.feature.DTSortable
import org.smartregister.fct.datatable.domain.model.DataCell
import org.smartregister.fct.datatable.domain.model.DataColumn
import org.smartregister.fct.datatable.domain.model.DataFilterColumn
import org.smartregister.fct.datatable.domain.model.DataFilterTypeColumn
import org.smartregister.fct.datatable.domain.model.DataRow
import org.smartregister.fct.datatable.presentation.ui.components.ColumnFilter
import org.smartregister.fct.datatable.presentation.ui.components.ColumnHeader
import org.smartregister.fct.datatable.presentation.ui.components.DTHorizontalDivider
import org.smartregister.fct.datatable.presentation.ui.components.PopulateData
import org.smartregister.fct.datatable.presentation.ui.components.TopBar
import org.smartregister.fct.engine.data.manager.AppSettingManager
import org.smartregister.fct.engine.util.getKoinInstance
import org.smartregister.fct.text_viewer.ui.dialog.rememberTextViewerDialog

internal val defaultDataCellWidth = 200.dp
internal val serialNoCellWidth = 60.dp


@Composable
fun DataTable(
    componentContext: ComponentContext,
    controller: DataTableController,
    columnLeadingIcon: (@Composable BoxScope.(DTColumn) -> Unit)? = null,
    customContextMenuItems: ((Int, DTColumn, Int, DataRow, DataCell) -> List<ContextMenuItem>)? = null
) {

    val error by controller.error.collectAsState()
    val info by controller.info.collectAsState()
    val horizontalScrollState = rememberScrollState()
    //val verticalScrollState = rememberScrollState()
    val dataRowBGOdd = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.3f)
    val dataRowBGEven = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.6f)
    var dtWidth by remember { mutableStateOf(0.dp) }
    val columns by controller.columns.collectAsState()

    Aurora(
        componentContext = componentContext
    ) {

        with(it) {
            showErrorSnackbar(error)
            showSnackbar(info)
        }

        val columnWidthMapState = remember {
            mutableStateMapOf<Int, Dp>().apply {
                columns.forEachIndexed { index, _ ->
                    put(index, defaultDataCellWidth)
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(controller)
            DTHorizontalDivider(dtWidth, alpha = 0.5f)
            Box(Modifier.fillMaxSize()) {
                Column(Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .height(40.dp)
                            .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.3f))
                            .horizontalScroll(horizontalScrollState)
                            .onGloballyPositioned {
                                dtWidth = it.size.width.dp
                            }
                    ) {
                        ColumnHeader(
                            controller = controller,
                            columns = columns,
                            columnWidthMapState = columnWidthMapState,
                            columnLeadingIcon = columnLeadingIcon
                        )
                    }
                    if (controller is DTFilterable) {
                        Row(
                            modifier = Modifier
                                .height(40.dp)
                                .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.6f))
                                .horizontalScroll(horizontalScrollState)
                        ) {
                            ColumnFilter(
                                controller = controller,
                                columns = columns,
                                columnWidthMapState = columnWidthMapState
                            )
                        }
                    }
                    DTHorizontalDivider(dtWidth, alpha = 0.5f)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .horizontalScroll(horizontalScrollState)
                    ) {
                        PopulateData(
                            controller = controller,
                            componentContext = componentContext,
                            columns = columns,
                            columnWidthMapState = columnWidthMapState,
                            dataRowBGOdd = dataRowBGOdd,
                            dataRowBGEven = dataRowBGEven,
                            dtWidth = dtWidth,
                            customContextMenuItems = customContextMenuItems
                        )
                    }
                }

                HorizontalScrollbar(
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                    adapter = rememberScrollbarAdapter(
                        scrollState = horizontalScrollState
                    )
                )
            }
        }
    }
}


private val VerticalScrollConsumer = object : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource) = available.copy(x = 0f)
    override suspend fun onPreFling(available: Velocity) = available.copy(x = 0f)
}

private val HorizontalScrollConsumer = object : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource) = available.copy(y = 0f)
    override suspend fun onPreFling(available: Velocity) = available.copy(y = 0f)
}

private fun Modifier.disableVerticalPointerInputScroll() = this.nestedScroll(VerticalScrollConsumer)

private fun Modifier.disableHorizontalPointerInputScroll() =
    this.nestedScroll(HorizontalScrollConsumer)



internal object ColumnType {
    const val FIELD_TYPE_NULL = 0
    const val FIELD_TYPE_INTEGER = 1
    const val FIELD_TYPE_FLOAT = 2
    const val FIELD_TYPE_STRING = 3
    const val FIELD_TYPE_BLOB = 4
}