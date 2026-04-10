package cc.atomtech.timetable.samples

import androidx.datastore.preferences.core.stringPreferencesKey
import cc.atomtech.timetable.preferences.PreferenceStore.preferences
import cc.atomtech.timetable.preferences.TransformerMutablePreference

enum class SampleEnum {
    SAMPLE_VAL_1,
    SAMPLE_VAL_2
}


val myTransformerMutablePreference = TransformerMutablePreference<SampleEnum, String>(
    initialValue = SampleEnum.SAMPLE_VAL_1,
    keyer = { stringPreferencesKey("MY_KEY") },
    preferences = preferences,
    storeTransformer = { value: SampleEnum ->
        return@TransformerMutablePreference value.name
    },
    readTransformer = { value: String ->
        return@TransformerMutablePreference SampleEnum.valueOf(value)
    }
)


