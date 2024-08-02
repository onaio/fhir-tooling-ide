package org.smartregister.fct.configs.util.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch


@Composable
fun <T> Flow<T>.flowAsState(
    key: Any? = null,
    initial: T,
): State<T> {
    var job: Job
    val state = remember(key) { mutableStateOf(initial) }

    DisposableEffect(key1 = key) {
        job = CoroutineScope(Dispatchers.IO).launch {
            this@flowAsState.cancellable().collect { newData ->
                state.value = newData
            }
        }

        onDispose {
            job.cancel()
        }
    }

    return state
}