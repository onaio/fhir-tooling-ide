package org.smartregister.fct.device_database.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.smartregister.fct.aurora.AuroraIconPack
import org.smartregister.fct.aurora.auroraiconpack.Table
import org.smartregister.fct.aurora.presentation.ui.components.LinearIndicator
import org.smartregister.fct.aurora.presentation.ui.components.TextButton
import org.smartregister.fct.device_database.domain.TableInfo
import java.awt.event.MouseEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun TablesList(
    listOfTables: List<TableInfo>,
    onTableDoubleClickc: (TableInfo) -> Unit,
    isTablesLoading: Boolean,
) {

    val scope = rememberCoroutineScope()

    HorizontalDivider()
    Box(
        Modifier.fillMaxWidth().height(36.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Tables",
            style = MaterialTheme.typography.titleSmall
        )
    }
    HorizontalDivider()
    if (isTablesLoading) {
        LinearIndicator()
        Text(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            text = "Loading table(s)",
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    }

    if (listOfTables.isEmpty()) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            text = "No table(s)",
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    }

    if (!isTablesLoading) {
        LazyColumn {

            item {
                Spacer(Modifier.height(8.dp))
            }
            items(listOfTables) { tableInfo ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onPointerEvent(PointerEventType.Press) {
                                when (it.awtEventOrNull?.button) {
                                    MouseEvent.BUTTON1 -> when (it.awtEventOrNull?.clickCount) {
                                        1 -> {}
                                        2 -> {
                                            scope.launch {
                                                delay(200)
                                                onTableDoubleClickc(tableInfo)
                                            }
                                        }
                                    }
                                }
                            },
                        iconModifier = Modifier.size(18.dp),
                        label = tableInfo.name,
                        icon = AuroraIconPack.Table,
                        textAlign = TextAlign.Start,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        onClick = {}
                    )
                }
            }
            item {
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}