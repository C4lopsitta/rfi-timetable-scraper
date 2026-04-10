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
 * The `TransformerMutablePreference` class helps to manage preferences tied to an application state
 * by storing an *internal state* that has each state change synced to a given DataStore preference
 * instance. It is not suggested to use this for constantly changing values as many I/O operations
 * would be triggered. If the state value and preferences-stored value do not need to be transformed
 * to be stored, use a [ModernMutablePreference].
 *
 * @property value Internal value store, when written it triggers an asynchronous write to the
 *                 given preference store.
 * @property keyer A lambda that provides a "keyer" to access the preference.
 * @property preferences The preference store.
 * @property storeTransformer A function that has as input a `value: <T>` and returns a value of type [S]
 * @property readTransformer A function that has as input a `value: <S>` and returns a value of type [T]
 * @param initialValue A value of type [T] that will be initially set as the internal state before
 *                     the asynchronous preference initialization can happen or the preference is
 *                     unset.
 * @param T The type of the internal state preference.
 * @param S The type of the preferences value.
 *
 * @sample cc.atomtech.timetable.samples.myTransformerMutablePreference
 * @see ModernMutablePreference
 */
class TransformerMutablePreference<T, S> (
    initialValue: T,
    private val keyer: () -> Preferences.Key<S>,
    private val preferences: DataStore<Preferences>?,
    private val storeTransformer: (value: T) -> S,
    private val readTransformer: (value: S) -> T
) : MutableState<T> {
    var internalState = mutableStateOf<T>(initialValue)

    override var value: T
        get() = internalState.value
        set(value) {
            internalState.value = value

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    preferences?.edit {
                        it[keyer()] = storeTransformer(value)
                    }
                } catch (e: CancellationException) {
                    throw e;
                } catch (e: Exception) {
                    e.printStackTrace();
                }
            }
        }

    override fun component1(): T = value

    override fun component2(): (T) -> Unit = { value = it }
}