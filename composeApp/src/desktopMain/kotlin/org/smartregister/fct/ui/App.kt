package org.smartregister.fct.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.smartregister.fct.configs.ui.ConfigManagerScreen
import org.smartregister.fct.engine.ui.viewmodel.SubWindowViewModel
import org.smartregister.fct.ui.components.BottomWindow
import org.smartregister.fct.ui.components.LeftNavigation
import org.smartregister.fct.ui.components.RightNavigation
import org.smartregister.fct.ui.components.RightWindow

@Composable
@Preview
fun App(subWindowViewModel: SubWindowViewModel) {

    var mainNavigator by remember { mutableStateOf<Navigator?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(modifier = Modifier.fillMaxSize()) {
            LeftNavigation(mainNavigator)
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
                    Navigator(ConfigManagerScreen()) {
                        mainNavigator = it
                        FadeTransition(it)
                    }
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