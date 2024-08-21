package org.smartregister.fct.fm.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.launch
import org.smartregister.fct.aurora.presentation.ui.components.LabelledCheckBox
import org.smartregister.fct.fm.domain.model.FileManagerMode
import org.smartregister.fct.fm.presentation.components.FileManagerComponent

@Composable
internal fun ContentOptions(
    component: FileManagerComponent,
    content: @Composable (RowScope.() -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    Box(
        Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.4f))
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
        ) {

            val (leftRef, middleRef, rightRef) = createRefs()

            BackButton(
                modifier = Modifier.constrainAs(leftRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                component = component
            )

            if (component.mode == FileManagerMode.Edit) {
                Row(
                    modifier = Modifier.constrainAs(middleRef) {
                        start.linkTo(leftRef.end)
                        top.linkTo(parent.top)
                        end.linkTo(rightRef.start)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.preferredWrapContent
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    content = content ?: {}
                )

                val showHiddenFile by component.getShowHiddenFile().collectAsState()

                LabelledCheckBox(
                    modifier = Modifier.constrainAs(rightRef) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                    checked = showHiddenFile,
                    label = "Hidden Files",
                    onCheckedChange = {
                        scope.launch {
                            component.setShowHiddenFile(it)
                        }
                    }
                )
            }
        }

        HorizontalDivider(Modifier.align(Alignment.BottomCenter))
    }
}