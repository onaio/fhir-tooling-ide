package org.smartregister.fct.rules.presentation.ui.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import fct.rules.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.skia.Image
import org.smartregister.fct.aurora.util.pxToDp
import org.smartregister.fct.rules.domain.model.BoardProperty
import org.smartregister.fct.rules.presentation.components.RulesScreenComponent
import java.awt.Point

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Board(
    component: RulesScreenComponent,
    content: @Composable BoxScope.(BoardProperty) -> Unit
) {

    val scrollSpeed = remember { 30 }
    val boardSize = component.boardSize
    val offset by component.boardOffset.collectAsState(IntOffset.Zero)
    val scale by component.boardScaling.collectAsState()

    val x by animateIntAsState(offset.x)
    val y by animateIntAsState(offset.y)

    Box(
        Modifier.scale(scale).wrapContentSize(unbounded = true)
            .onPointerEvent(PointerEventType.Scroll) {
                component.updateBoardOffset(
                    IntOffset(
                        x = offset.x + (it.changes.first().scrollDelta.x.toInt() * scrollSpeed).inv(),
                        y = offset.y + (it.changes.first().scrollDelta.y.toInt() * scrollSpeed).inv()
                    )
                )
            }) {

        Box(modifier = Modifier
            .offset(x.dp, y.dp)
            .size(boardSize.width.pxToDp(), boardSize.height.pxToDp())
            .patternBackground(MaterialTheme.colorScheme.surface)
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    component.updateBoardOffset(
                        IntOffset(
                            x = offset.x + dragAmount.x.toInt(),
                            y = offset.y + dragAmount.y.toInt()
                        )
                    )
                }
            }
        ) {
            content(
                BoardProperty(
                    width = boardSize.width.toInt(),
                    height = boardSize.height.toInt(),
                    center = Point((boardSize.width / 2).toInt(), (boardSize.height / 2).toInt())
                )
            )
        }
    }

}


@OptIn(ExperimentalResourceApi::class)
@Composable
private fun Modifier.patternBackground(color: Color): Modifier {

    val image = remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(image) {
        image.value =
            Image.makeFromEncoded(Res.readBytes("drawable/pattern.png")).toComposeImageBitmap()
    }

    return if (image.value != null) {
        val imageBrush = remember(image) {
            ShaderBrush(
                shader = ImageShader(
                    image = image.value!!,
                    tileModeX = TileMode.Repeated,
                    tileModeY = TileMode.Repeated,
                )
            )
        }

        this.drawBehind {
            drawRect(
                brush = imageBrush,
                colorFilter = ColorFilter.tint(color = color)
            )
        }
    } else Modifier
}