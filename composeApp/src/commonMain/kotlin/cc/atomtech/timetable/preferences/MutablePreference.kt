package cc.atomtech.timetable.preferences

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MutablePreference<T> (
    initialValue: T,
    private val coroutineScope: CoroutineScope,
    private val updatePreference: suspend (T) -> Unit
) : MutableState<T> {
    var internalState = mutableStateOf<T>(initialValue)

    override var value: T
        get() = internalState.value
        set(value) {
            internalState.value = value

            coroutineScope.launch {
                updatePreference(value)
            }
        }

    override fun component1(): T = value

    override fun component2(): (T) -> Unit = { value = it }
}