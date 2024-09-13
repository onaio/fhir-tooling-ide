package org.smartregister.fct.datatable.presentation.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.HorizontalScrollbar
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
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.smartregister.fct.aurora.AuroraIconPack
import org.smartregister.fct.aurora.auroraiconpack.Sync
import org.smartregister.fct.aurora.presentation.ui.components.AutoCompleteDropDown
import org.smartregister.fct.aurora.presentation.ui.components.SmallIconButton
import org.smartregister.fct.aurora.presentation.ui.components.Tooltip
import org.smartregister.fct.aurora.presentation.ui.components.TooltipPosition
import org.smartregister.fct.common.util.windowWidthResizePointer
import org.smartregister.fct.datatable.data.controller.DataTableController
import org.smartregister.fct.engine.data.manager.AppSettingManager
import org.smartregister.fct.engine.util.getKoinInstance
import org.smartregister.fct.text_viewer.ui.dialog.rememberTextViewerDialog

private val defaultDataCellWidth = 200.dp
private val serialNoCellWidth = 60.dp

@Composable
fun DataTable(
    controller: DataTableController,
    componentContext: ComponentContext,
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
                put(index, defaultDataCellWidth)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DTTopBar(controller)
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .horizontalScroll(horizontalScrollState)
                ) {
                    PopulateData(
                        controller = controller,
                        componentContext = componentContext,
                        columnsInfo = columns,
                        columnWidthMapState = columnWidthMapState,
                        dataRowBGOdd = dataRowBGOdd,
                        dataRowBGEven = dataRowBGEven,
                        dtWidth = dtWidth
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
                    controller.goNext()
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

@Composable
internal fun ColumnHeader(
    controller: DataTableController,
    columnsInfo: List<ColumnInfo>,
    columnWidthMapState: SnapshotStateMap<Int, Dp>,
) {
    Row(Modifier.fillMaxHeight()) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(serialNoCellWidth)
                .background(MaterialTheme.colorScheme.surfaceContainer),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "S.No",
                style = MaterialTheme.typography.titleSmall
            )

            DTVerticalDivider(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                alpha = 0.5f
            )
        }

        columnsInfo.forEach { colView ->

            var rawX by remember {
                mutableStateOf(
                    columnWidthMapState[colView.index] ?: defaultDataCellWidth
                )
            }

            DataBox(
                index = colView.index,
                columnWidthMapState = columnWidthMapState
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = colView.name,
                    style = MaterialTheme.typography.titleSmall
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
    Row(Modifier.fillMaxHeight()) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(serialNoCellWidth)
                .background(MaterialTheme.colorScheme.surfaceContainer),
        ) {
            DTVerticalDivider(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                alpha = 0.5f
            )
        }

        columnsInfo.forEach {

            DataBox(
                index = it.index,
                columnWidthMapState = columnWidthMapState
            ) {
                DataFilterTextField(
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
    componentContext: ComponentContext,
    columnsInfo: List<ColumnInfo>,
    columnWidthMapState: SnapshotStateMap<Int, Dp>,
    dataRowBGOdd: Color,
    dataRowBGEven: Color,
    dtWidth: Dp,
) {

    val clipboardManager = LocalClipboardManager.current
    val textViewerDialog = rememberTextViewerDialog(componentContext)
    val data by controller.records.collectAsState()
    var dataRowHover by remember { mutableStateOf(-1) }
    var dataCellHover by remember { mutableStateOf(-1) }

    LazyColumn {

        itemsIndexed(data) { rowIndex, dataObj ->

            val dataRowBG = if (dataRowHover == rowIndex) {
                MaterialTheme.colorScheme.surfaceContainer
            } else {
                if (rowIndex % 2 == 0) dataRowBGEven else dataRowBGOdd
            }

            Row(
                modifier = Modifier
                    .height(40.dp)
                    .background(dataRowBG),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(serialNoCellWidth)
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "${controller.getOffset() + rowIndex + 1}"
                    )

                    DTVerticalDivider(
                        modifier = Modifier
                            .align(Alignment.CenterEnd),
                        alpha = 0.5f
                    )
                }

                columnsInfo.forEachIndexed { colIndex, columnInfo ->

                    val dataCellBorder =
                        if (rowIndex == dataRowHover && colIndex == dataCellHover) BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                        ) else null

                    DataBox(
                        modifier = Modifier
                            .onPointerEvent(PointerEventType.Enter) {
                                dataRowHover = rowIndex
                                dataCellHover = colIndex
                            }
                            .onPointerEvent(PointerEventType.Exit) {
                                dataRowHover = -1
                                dataCellHover = -1
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

                        Surface(
                            modifier = Modifier.fillMaxSize().background(Color.Transparent),
                            border = dataCellBorder,
                            color = Color.Transparent
                        ) {
                            Tooltip(
                                modifier = Modifier.fillMaxSize(),
                                tooltip = text,
                                tooltipPosition = TooltipPosition.Top(space = 10),
                            ) {
                                ContextMenuArea(
                                    items = {
                                        listOf(
                                            ContextMenuItem("Copy") {
                                                clipboardManager.setText(
                                                    AnnotatedString(columnInfo.getData(dataObj))
                                                )
                                            },
                                            ContextMenuItem("View") {
                                                textViewerDialog.show(columnInfo.getData(dataObj))
                                            },
                                        )
                                    },
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxSize()
                                            .padding(horizontal = 8.dp),
                                        text = text,
                                        softWrap = false,
                                        lineHeight = 32.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (rowIndex == data.size.minus(1)) {
                DTHorizontalDivider(dtWidth)
            }
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
            .width(columnWidthMapState[index] ?: defaultDataCellWidth)
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
        color = if (theme.appSetting.isDarkTheme) DividerDefaults.color else DividerDefaults.color.copy(
            alpha = alpha
        )
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
        color = if (theme.appSetting.isDarkTheme) MaterialTheme.colorScheme.outline else DividerDefaults.color.copy(
            alpha = alpha
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataFilterTextField(
    controller: DataTableController,
    placeholder: String
) {

    var filterText by remember { mutableStateOf("") }

    filterText.useDebounce {
        println(it)
    }

    BasicTextField(
        modifier = Modifier.fillMaxSize(),
        value = filterText,
        onValueChange = {
            if (it.length > 40) return@BasicTextField
            filterText = it
        },
        textStyle = TextStyle(
            fontSize = MaterialTheme.typography.bodySmall.fontSize
        ),
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon
            TextFieldDefaults.DecorationBox(
                value = filterText,
                visualTransformation = VisualTransformation.None,
                innerTextField = innerTextField,
                placeholder = {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                    )
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
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.2f),
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        }
    )
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

@Composable
fun <T> T.useDebounce(
    delayMillis: Long = 300L,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onChange: (T) -> Unit
): T {
    // 2. updating state
    val state by rememberUpdatedState(this)

    // 3. launching the side-effect handler
    DisposableEffect(state) {
        val job = coroutineScope.launch {
            delay(delayMillis)
            onChange(state)
        }
        onDispose {
            job.cancel()
        }
    }
    return state
}

private fun ColumnInfo.getData(jsonObject: JSONObject): String {
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