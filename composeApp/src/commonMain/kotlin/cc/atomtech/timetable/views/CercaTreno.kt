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
import androidx.compose.material3.HorizontalDivider
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
import cc.atomtech.timetable.components.TrainStopList
import cc.atomtech.timetable.components.train.TrainHeader
import cc.atomtech.timetable.enumerations.CurrentStationType
import cc.atomtech.timetable.enumerations.ui.TrainHeaderDisplayOrder
import cc.atomtech.timetable.models.rfi.TrainStopData
import cc.atomtech.timetable.models.trenitalia.CercaTrenoData
import cc.atomtech.timetable.models.trenitalia.RestEasyTrainData
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/** Searches for a train by its number. Currently only supports *Trenitalia* trains.
 *
 * @since 1.5.0
 * @see TrenitaliaRestEasy
 * @see CercaTrenoData
 * @author Simone Robaldo
 */
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

        queryResult = if(trainNumberQuery.isNotEmpty()) {
            TrenitaliaRestEasy.searchTrainByNumber(trainNumberQuery)
        } else {
            trainNumberQueryIsError = true
            println("Failed fetching train")
            null
        }

        if(queryResult != null) {
            try {
                queryTrainData = TrenitaliaRestEasy.fetchTrain(queryResult!!)
            } catch(ex: Exception) {
                queryResult = null
                trainNumberQueryIsError = true
                println(ex)
            }
        } else {
            queryTrainData = null
            trainNumberQueryIsError = true
        }

        trainNumberQueryIsError = false
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
                    TrainHeader(queryTrainData!!, displayOrder = TrainHeaderDisplayOrder.ORIGIN_FIRST)
                }
//                item {
//                    Text("Departure time ${queryTrainData?.departureTime}")
//                    Text("Arrival time ${queryTrainData?.arrivalTime}")
//                    Text("Delay ${queryTrainData?.delayReason}")
//                    Text("Delay reason ${queryTrainData?.delayReason}")
//                    Text("Category ${queryTrainData?.category}")
//                    Text("Has Departed: ${queryTrainData?.hasYetToDepart}")
//                }
                item {
                    HorizontalDivider()
                    TrainStopList(
                        stationType = CurrentStationType.LINE_START,
                        delay = 0,
                        stops = List(queryTrainData?.stops?.size ?: 0) { index ->
                            val timeZone = TimeZone.currentSystemDefault()
                            val localTime = Instant
                                .fromEpochMilliseconds(
                                    queryTrainData?.stops?.get(index)?.actualTime ?:
                                    queryTrainData?.stops?.get(index)?.scheduledTime ?: 0L
                                )
                                .toLocalDateTime(timeZone)

                            TrainStopData(
                                name = queryTrainData?.stops?.get(index)?.stationName ?: "",
                                time = "${localTime.hour}:${localTime.minute.toString().padStart(2, '0')}",
                                isCurrentStop = index == 0
                            )
                        }
                    )
                }
                item {
                    Text(queryTrainData.toString())
                }
            }
        }
    }
}
