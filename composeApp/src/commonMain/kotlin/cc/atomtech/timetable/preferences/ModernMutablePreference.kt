package cc.atomtech.timetable.preferences

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The `ModernMutablePreference` class helps to manage preferences tied to an application state by
 * storing an *internal state* that has each state change synced to a given DataStore preference
 * instance. It is not suggested to use this for constantly changing values as many I/O operations
 * would be triggered. If the state value and preferences-stored value do not share a type, check
 * the [TransformerMutablePreference].
 *
 * @property value Internal value store, when written it triggers an asynchronous write to the
 *                 given preference store.
 * @property keyer A lambda that provides a "keyer" to access the preference.
 * @property preferences The preference store.
 * @param initialValue A value of type [T] that will be initially set as the internal state before
 *                     the asynchronous preference initialization can happen or the preference is
 *                     unset.
 * @param T The type of the preference.
 *
 * @sample cc.atomtech.timetable.samples.myMutablePreference
 * @see TransformerMutablePreference
 */
class ModernMutablePreference<T> (
    initialValue: T,
    private val keyer: () -> Preferences.Key<T>,
    private val preferences: DataStore<Preferences>?
) : MutableState<T> {
    var internalState = mutableStateOf<T>(initialValue)

    init {

    }

    override var value: T
        get() = internalState.value
        set(value) {
            internalState.value = value

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    preferences?.edit {
                        it[keyer()] = value
                    }
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    e.printStackTrace()
                }
            }
        }

    override fun component1(): T = value

    override fun component2(): (T) -> Unit = { value = it }
}