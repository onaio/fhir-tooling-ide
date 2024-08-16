package org.smartregister.fct.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.smartregister.fct.engine.presentation.component.RootComponent
import org.smartregister.fct.engine.presentation.viewmodel.SubWindowViewModel
import org.smartregister.fct.presentation.ui.components.BottomWindow
import org.smartregister.fct.presentation.ui.components.LeftNavigation
import org.smartregister.fct.presentation.ui.components.MainRoot
import org.smartregister.fct.presentation.ui.components.RightNavigation
import org.smartregister.fct.presentation.ui.components.RightWindow

@Composable
@Preview
fun App(
    rootComponent: RootComponent,
    subWindowViewModel: SubWindowViewModel
) {

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(modifier = Modifier.fillMaxSize()) {
            LeftNavigation(rootComponent = rootComponent)
            VerticalDivider()
            ConstraintLayout {
                val (body, rightWindow, rightNavBar, bottomWindow) = createRefs()

                Box(modifier = Modifier.fillMaxSize().constrainAs(body) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(rightWindow.start)
                    bottom.linkTo(bottomWindow.top)
                    width = Dimension.preferredWrapContent
                    height = Dimension.preferredWrapContent
                }) {
                    MainRoot(rootComponent)
                }

                Row(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                        .constrainAs(rightWindow) {
                            top.linkTo(parent.top)
                            end.linkTo(rightNavBar.start)
                            bottom.linkTo(bottomWindow.top)
                            height = Dimension.preferredWrapContent
                        }) {
                    RightWindow(subWindowViewModel)
                }

                Row(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                        .constrainAs(rightNavBar) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }) {
                    RightNavigation(subWindowViewModel)
                }

                Column(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
                        .constrainAs(bottomWindow) {
                            start.linkTo(parent.start)
                            end.linkTo(rightNavBar.start)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.preferredWrapContent
                        }) {
                    BottomWindow(subWindowViewModel)
                }
            }

        }
    }

}