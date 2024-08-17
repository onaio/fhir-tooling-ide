package org.smartregister.fct.fm.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import okio.Path

@Composable
fun ConstraintLayoutScope.Breadcrumb(
    pathRef: ConstrainedLayoutReference,
    path: Path
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.8f))
            .constrainAs(pathRef) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
    ) {
        HorizontalDivider()
        Text(
            modifier = Modifier.padding(6.dp),
            text = path.toString(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodySmall
        )
    }
}