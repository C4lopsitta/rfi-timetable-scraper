package cc.atomtech.timetable.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.apis.TrenitaliaRestEasy
import cc.atomtech.timetable.models.TrenitaliaTrainData

@Composable
fun CercaTreno() {
    var trainNumberQuery by remember { mutableStateOf("") }
    var trainNumberQueryIsError by remember { mutableStateOf(false) }
    var queryResults by remember { mutableStateOf<List<TrenitaliaTrainData>>(emptyList()) }

    LaunchedEffect(trainNumberQuery) {
        if(!trainNumberQuery.matches(Regex("[0-9]+"))) {
            queryResults = emptyList()
            trainNumberQueryIsError = true
            return@LaunchedEffect
        }

        trainNumberQueryIsError = false

        queryResults = if(trainNumberQuery.isNotEmpty()) {
            TrenitaliaRestEasy.fetchTrainByNumber(trainNumberQuery)
        } else {
            emptyList()
        }
    }

    Column (
        modifier = Modifier.fillMaxSize().padding( top = 12.dp, end = 12.dp )
    ) {
        Card (
            modifier = Modifier.padding( vertical = 4.dp ),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                disabledContentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Text("Beta Preview", modifier = Modifier.padding( horizontal = 8.dp ))
        }
        Text(
            StringRes.get("cerca_treno_title"),
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding( bottom = 12.dp )
        )
        OutlinedTextField(
            value = trainNumberQuery,
            onValueChange = { trainNumberQuery = it },
            label = { Text(StringRes.get("txt_train_number")) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = trainNumberQueryIsError,
            singleLine = true,
        )
        Row (
            modifier = Modifier.height( 20.dp ).padding( vertical = 8.dp ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.Info, contentDescription = StringRes.get("icon_notice"))
//            Box( modifier = Modifier.padding( horizontal = 4.dp ) )
            Text(StringRes.get("cerca_treno_notice"))
        }
        LazyColumn {
            items (queryResults) { result ->
                Text("(${result.number}) ${result.originStationId}")
            }
        }
    }
}
