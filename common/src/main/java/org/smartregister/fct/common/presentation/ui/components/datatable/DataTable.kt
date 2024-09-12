package org.smartregister.fct.common.presentation.ui.components.datatable

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.LastPage
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FirstPage
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.areAnyPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.json.JSONObject
import org.smartregister.fct.aurora.AuroraIconPack
import org.smartregister.fct.aurora.auroraiconpack.Sync
import org.smartregister.fct.aurora.presentation.ui.components.AutoCompleteDropDown
import org.smartregister.fct.aurora.presentation.ui.components.SmallIconButton
import org.smartregister.fct.aurora.presentation.ui.components.Tooltip
import org.smartregister.fct.aurora.presentation.ui.components.TooltipPosition
import org.smartregister.fct.common.data.datatable.controller.DataTableController
import org.smartregister.fct.common.data.datatable.feature.DTPagination
import org.smartregister.fct.common.util.windowWidthResizePointer
import org.smartregister.fct.engine.data.manager.AppSettingManager
import org.smartregister.fct.engine.domain.model.AppSetting
import org.smartregister.fct.engine.util.getKoinInstance

@Composable
fun DataTable(
    controller: DataTableController
) {

    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()
    val dataRowBGOdd = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.3f)
    val dataRowBGEven = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.6f)
    var dtWidth by remember { mutableStateOf(0.dp) }


    val columns = controller.columns.mapIndexed { index, columnObj ->
        ColumnInfo(
            index = index,
            name = columnObj.getString("name"),
            type = columnObj.getInt("type")
        )
    }

    val columnWidthMapState = remember {
        mutableStateMapOf<Int, Dp>().apply {
            columns.forEachIndexed { index, _ ->
                put(index, 200.dp)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DTTopBar(controller)
        DTHorizontalDivider(dtWidth, alpha = 0.5f)
        Box (Modifier.fillMaxSize()) {
            Column (Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.3f))
                        .horizontalScroll(horizontalScrollState)
                        .onGloballyPositioned {
                            dtWidth = it.size.width.dp
                        }
                ) {
                    ColumnHeader (
                        controller = controller,
                        columnsInfo = columns,
                        columnWidthMapState = columnWidthMapState
                    )
                }
                //DTHorizontalDivider(dtWidth, alpha = 0.4f)
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.6f))
                        .horizontalScroll(horizontalScrollState)
                ) {
                    ColumnFilter(
                        controller = controller,
                        columnsInfo = columns,
                        columnWidthMapState = columnWidthMapState
                    )
                }
                DTHorizontalDivider(dtWidth, alpha = 0.5f)
                Box {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(verticalScrollState)
                            .horizontalScroll(horizontalScrollState)
                    ) {
                        PopulateData(
                            controller = controller,
                            columnsInfo = columns,
                            columnWidthMapState = columnWidthMapState,
                            dataRowBGOdd = dataRowBGOdd,
                            dataRowBGEven = dataRowBGEven,
                            dtWidth = dtWidth
                        )
                    }

                    VerticalScrollbar(
                        modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                        adapter = rememberScrollbarAdapter(
                            scrollState = verticalScrollState
                        )
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

@Composable
fun DTTopBar(
    controller: DataTableController
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        SmallIconButton(
            modifier = Modifier.size(18.dp),
            icon = AuroraIconPack.Sync,
            onClick = controller::refreshData,
        )

        if (controller is DTPagination) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SmallIconButton(
                    modifier = Modifier.size(22.dp),
                    icon = Icons.Outlined.FirstPage,
                    onClick = controller::refreshData
                )
                Spacer(Modifier.width(12.dp))
                SmallIconButton(
                    modifier = Modifier.size(22.dp),
                    icon = Icons.Outlined.ChevronLeft,
                    onClick = controller::refreshData
                )
                Spacer(Modifier.width(12.dp))
                AutoCompleteDropDown(
                    modifier = Modifier.width(100.dp),
                    items = listOf(50, 100, 500, 1000),
                    label = { "$it" },
                    defaultSelectedIndex = 0,
                    errorHighlight = false,
                    onTextChanged = { text, isMatch ->

                    },
                    onSelected = {

                    }
                )
                Spacer(Modifier.width(12.dp))
                SmallIconButton(
                    modifier = Modifier.size(22.dp),
                    icon = Icons.Outlined.ChevronRight,
                    onClick = {
                        controller.goNext(
                            controller.getOffset() + controller.getLimit()
                        )
                    }
                )
                Spacer(Modifier.width(12.dp))
                SmallIconButton(
                    modifier = Modifier.size(22.dp),
                    icon = Icons.AutoMirrored.Outlined.LastPage,
                    onClick = controller::refreshData
                )
                Spacer(Modifier.width(8.dp))
            }
        }
    }
}

@Composable
internal fun ColumnHeader(
    controller: DataTableController,
    columnsInfo: List<ColumnInfo>,
    columnWidthMapState: SnapshotStateMap<Int, Dp>,
) {
    Row(Modifier.fillMaxHeight()) {

        columnsInfo.forEach { colView ->

                var rawX by remember { mutableStateOf(columnWidthMapState[colView.index] ?: 200.dp) }

                DataBox(
                    index = colView.index,
                    columnWidthMapState = columnWidthMapState
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = colView.name
                    )

                    Box(
                        modifier = Modifier
                            .width(5.dp)
                            .fillMaxHeight()
                            .align(Alignment.CenterEnd)
                            .pointerHoverIcon(windowWidthResizePointer)
                            .pointerInput(Unit) {
                                detectHorizontalDragGestures(
                                    onDragStart = {
                                        rawX = columnWidthMapState[colView.index]!!
                                    }
                                ) { _, dragAmount ->
                                    rawX += dragAmount.toDp()

                                    if (rawX > 40.dp) {
                                        columnWidthMapState[colView.index] = rawX.coerceAtLeast(40.dp)
                                    }
                                }
                            }
                    )
                }
            }


    }
}

@Composable
internal fun ColumnFilter(
    controller: DataTableController,
    columnsInfo: List<ColumnInfo>,
    columnWidthMapState: SnapshotStateMap<Int, Dp>,
) {
    Row (Modifier.fillMaxHeight()) {
        columnsInfo.forEach {

            DataBox(
                index = it.index,
                columnWidthMapState =columnWidthMapState
            ) {
                DataFilterTextField (
                    controller = controller,
                    placeholder = it.name
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun PopulateData(
    controller: DataTableController,
    columnsInfo: List<ColumnInfo>,
    columnWidthMapState: SnapshotStateMap<Int, Dp>,
    dataRowBGOdd: Color,
    dataRowBGEven: Color,
    dtWidth: Dp,
) {

    val data by controller.data.collectAsState()
    var dataRowHover by remember { mutableStateOf(-1) }

    data.forEachIndexed { rowIndex, dataObj ->

        val dataRowBG = if (dataRowHover == rowIndex) {
            MaterialTheme.colorScheme.surfaceContainer
        } else {
            if (rowIndex % 2 == 0) dataRowBGEven else dataRowBGOdd
        }

        Row (
            modifier = Modifier
                .height(40.dp)
                .background(dataRowBG)
        ) {
            columnsInfo.forEachIndexed { colIndex, columnInfo ->

                DataBox(
                    modifier = Modifier
                        .onPointerEvent(PointerEventType.Enter) {
                            dataRowHover = rowIndex
                        }
                        .onPointerEvent(PointerEventType.Exit) {
                            dataRowHover = -1
                        },
                    index = columnInfo.index,
                    columnWidthMapState = columnWidthMapState,
                    enableDivider = colIndex == columnsInfo.size.minus(1)
                ) {

                    val text = columnInfo.getData(dataObj).let {
                        if (it.length > 40) {
                            "${it.substring(0, 40)}..."
                        } else it
                    }
                    Tooltip(
                        tooltip = text,
                        tooltipPosition = TooltipPosition.TOP,
                    ) {
                        Text(
                            modifier = Modifier.padding(6.dp),
                            text = text,
                            softWrap = false
                        )
                    }
                }
            }
        }

        if (rowIndex == data.size.minus(1)) {
            DTHorizontalDivider(dtWidth,)
        }
    }

}

@Composable
internal fun DataBox(
    modifier: Modifier = Modifier,
    index: Int,
    columnWidthMapState: SnapshotStateMap<Int, Dp>,
    enableDivider: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .width(columnWidthMapState[index] ?: 200.dp)
            .fillMaxHeight(),
    ) {
        content()

        if (enableDivider) {
            DTVerticalDivider(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                alpha = 0.5f
            )
        }
    }
}

@Composable
internal fun DTHorizontalDivider(
    dtWidth: Dp,
    alpha: Float = 1f
) {
    val theme = getKoinInstance<AppSettingManager>()

    HorizontalDivider(
        modifier = Modifier.width(dtWidth),
        color = if (theme.appSetting.isDarkTheme) DividerDefaults.color else DividerDefaults.color.copy(alpha = alpha)
    )
}

@Composable
internal fun DTVerticalDivider(
    modifier: Modifier = Modifier,
    alpha: Float = 1f
) {
    val theme = getKoinInstance<AppSettingManager>()
    VerticalDivider(
        modifier = modifier,
        thickness = if (theme.appSetting.isDarkTheme) 0.3.dp else 1.dp,
        color = if (theme.appSetting.isDarkTheme) MaterialTheme.colorScheme.outline else DividerDefaults.color.copy(alpha = alpha)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataFilterTextField(
    controller: DataTableController,
    placeholder: String
) {

    BasicTextField (
        modifier = Modifier.fillMaxSize(),
        value = "",
        onValueChange = {},
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon
            TextFieldDefaults.DecorationBox(
                value = "",
                visualTransformation = VisualTransformation.None,
                innerTextField = innerTextField,
                placeholder = {
                    Text(placeholder)
                },
                label = null,
                leadingIcon = null,
                trailingIcon = null,
                prefix = null,
                suffix = null,
                supportingText = null,
                shape = TextFieldDefaults.shape,
                singleLine = true,
                enabled = true,
                isError = false,
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = PaddingValues(8.dp),
                colors = TextFieldDefaults.colors(),
            )
        }
    )
}

private fun ColumnInfo.getData(jsonObject: JSONObject) : String {
    return try {
        jsonObject.optString(name, "NULL")
    } catch (ex: Exception) {
        "NULL"
    }
}

internal data class ColumnInfo(
    //val ref: ConstrainedLayoutReference,
    val index: Int,
    val name: String,
    val type: Int
)

internal object ColumnType {
    const val FIELD_TYPE_NULL = 0
    const val FIELD_TYPE_INTEGER = 1
    const val FIELD_TYPE_FLOAT = 2
    const val FIELD_TYPE_STRING = 3
    const val FIELD_TYPE_BLOB = 4
}