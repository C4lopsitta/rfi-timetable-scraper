package cc.atomtech.timetable.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import cc.atomtech.timetable.components.train.TrainHeader
import cc.atomtech.timetable.models.trenitalia.CercaTrenoData
import cc.atomtech.timetable.models.trenitalia.RestEasyTrainData

@Composable
fun CercaTreno() {
    var trainNumberQuery by remember { mutableStateOf("") }
    var trainNumberQueryIsError by remember { mutableStateOf(false) }
    var queryResult by remember { mutableStateOf<CercaTrenoData?>(null) }
    var queryTrainData by remember { mutableStateOf<RestEasyTrainData?>(null) }

    LaunchedEffect(trainNumberQuery) {
        trainNumberQuery.trim()

        if(!trainNumberQuery.matches(Regex("[0-9]+"))) {
            queryResult = null
            queryTrainData = null
            trainNumberQueryIsError = trainNumberQuery.isNotEmpty()
            return@LaunchedEffect
        }

        trainNumberQueryIsError = false

        queryResult = if(trainNumberQuery.isNotEmpty()) {
            TrenitaliaRestEasy.searchTrainByNumber(trainNumberQuery)
        } else {
            null
        }

        if(queryResult != null) {
            queryTrainData = TrenitaliaRestEasy.fetchTrain(queryResult!!)
        } else {
            queryTrainData = null
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
            Text("Beta", modifier = Modifier.padding( horizontal = 8.dp ))
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
        if(trainNumberQueryIsError) Text(StringRes.get("error_train_number"), color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.padding( top = 4.dp ))
        Row (
            modifier = Modifier.height( 32.dp ).padding( vertical = 8.dp ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.Info, contentDescription = StringRes.get("icon_notice"), modifier = Modifier.width(24.dp).height(24.dp))
            Box( modifier = Modifier.padding( horizontal = 2.dp ) )
            Text(StringRes.get("cerca_treno_notice"), fontSize = 12.sp, lineHeight = 12.sp)
        }
//        if(queryResult != null)
//            Text("(${queryResult!!.number}) ${queryResult!!.originName}")

        if(queryTrainData != null) {
            LazyColumn {
                item {
                    TrainHeader(queryTrainData!!)
                }
//                Text("Origin ${queryTrainData!!.origin}")
//                Text("Destination ${queryTrainData!!.destination}")
//                Text("Train number ${queryTrainData!!.trainNumber}")
//                Text("Departure time ${queryTrainData!!.departureTime}")
//                Text("Arrival time ${queryTrainData!!.arrivalTime}")
//                Text("Delay ${queryTrainData!!.delayReason}")
            }
        }
    }
}
