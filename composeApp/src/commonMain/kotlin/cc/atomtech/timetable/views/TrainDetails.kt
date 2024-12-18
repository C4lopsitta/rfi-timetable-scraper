package cc.atomtech.timetable.views

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.components.TrainStopList
import cc.atomtech.timetable.enumerations.Category
import cc.atomtech.timetable.enumerations.CurrentStationType
import cc.atomtech.timetable.enumerations.Operator
import cc.atomtech.timetable.models.DetailedTrainData
import cc.atomtech.timetable.models.TrenitaliaTrainData
import cc.atomtech.timetable.models.TrenitaliaTrainDetails
import cc.atomtech.timetable.scrapers.TrenitaliaScraper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Composable
fun DataPairRow(
    labelLeft: String,
    dataLeft: String,
    labelRight: String,
    datRight: String
) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Text( labelLeft )
            Text(dataLeft, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        }
        Column (
            horizontalAlignment = Alignment.End
        ) {
            Text( labelRight )
            Text( datRight, fontSize = 24.sp, fontWeight = FontWeight.SemiBold )
        }
    }
}


@Composable
fun TrainDetails(trainData: DetailedTrainData,
                 isArrival: Boolean) {
    var availableTrains by remember { mutableStateOf<List<TrenitaliaTrainData>?>(null) }
    var trenitaliaAdvancedTrainData by remember { mutableStateOf<TrenitaliaTrainData?>(null) }
    var specificTrenitaliaDetails by remember { mutableStateOf<TrenitaliaTrainDetails?>(null) }

    LaunchedEffect(Unit) {
        try {
            availableTrains = if(trainData.operator == Operator.TRENITALIA && trainData.category != Category.BUS)
                TrenitaliaScraper.fetchTrainByNumber(trainData.number)
                else listOf()

            if(availableTrains?.isNotEmpty() == true) {
                specificTrenitaliaDetails = TrenitaliaScraper.getAndamentoTreno(availableTrains!![0])

                println(specificTrenitaliaDetails)
            }
        } catch (e: Exception) {
            println(e.printStackTrace())
            println("Failed fetching Trenitalia specific train data")
        }
    }

    Column (
        modifier = Modifier.fillMaxSize().padding( end = 12.dp ).verticalScroll(rememberScrollState())
    ) {
        // number with "to" text TODO)) Add from in case arrival
        Text(StringRes.format("details_number", trainData.number))
        Text(
            trainData.arrival,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 38.sp
        )

        // "from" text if not line start
        if(trainData.currentStationType == CurrentStationType.STOP) {
            Text(
                StringRes.get("from"),
                modifier = Modifier.padding( top = 4.dp ).fillMaxWidth(),
//                textAlign = TextAlign.End
            )
            Text(
                trainData.departure,
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 38.sp,
//                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        HorizontalDivider( modifier = Modifier.padding( vertical = 12.dp ) )

        if(trainData.currentStationType == CurrentStationType.STOP) {
            // show times, then delay plus platform
            DataPairRow(
                StringRes.get("details_arrives"),
                trainData.arrivesAt,
                StringRes.get("details_departs"),
                trainData.departsAt
            )

            HorizontalDivider( modifier = Modifier.padding( vertical = 12.dp ) )

            DataPairRow(
                StringRes.get("delay"),
                trainData.delay ?: StringRes.get("on_time"),
                StringRes.get("details_from_platform"),
                trainData.platform ?: StringRes.get("platform_to_be_announced")
            )
        } else {
            val isBeginning = trainData.currentStationType == CurrentStationType.LINE_START

            // show time plus platform, then delay
            DataPairRow(
                if(isBeginning) StringRes.get("details_departs") else StringRes.get("details_arrives"),
                if(isBeginning) trainData.departsAt else trainData.arrivesAt,
                StringRes.get("details_from_platform"),
                trainData.platform ?: StringRes.get("platform_to_be_announced")
            )

            if(trainData.delay != null) {
                DataPairRow(
                    StringRes.get("delay"),
                    trainData.delay,
                    "", ""
                )
            }
        }

        HorizontalDivider( modifier = Modifier.padding( vertical = 12.dp ) )

        DataPairRow(
            StringRes.get("operator"),
            trainData.operator.toString(),
            StringRes.get("service"),
            trainData.category.toString()
        )

        if(trainData.details != null) {
            if(trainData.details.isNotEmpty()) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Text(StringRes.get("details"))
                Text(trainData.details)
            }
        }

        if(trainData.operator == Operator.TRENITALIA && trainData.category != Category.BUS) {
            val urlHandler = LocalUriHandler.current

            HorizontalDivider( modifier = Modifier.padding( vertical = 12.dp ) )
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = LocalIndication.current,
                        role  = Role.Button,
                        onClickLabel = StringRes.get("click_for_details"),
                        onClick = {
                            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                                var url = "https://www.viaggiatreno.it/"

                                try {
                                    if (availableTrains != null && availableTrains?.size == 1) {
                                        url =
                                            TrenitaliaScraper.getViaggiaTrenoUrl(availableTrains!!.first())
                                    } else {
                                        var train = availableTrains!!.first()
                                        availableTrains!!.forEach { t ->
                                            if (t.departureTime.toLong() > train.departureTime.toLong()) train =
                                                t
                                        }
                                        url = TrenitaliaScraper.getViaggiaTrenoUrl(train)
                                    }
                                } catch (_: Exception) {

                                }

                                urlHandler.openUri(url)
                            }
                        }
                    )
            ) {
                Text(StringRes.get("open_in_viaggiatreno"))
                Icon(Icons.AutoMirrored.Rounded.OpenInNew, contentDescription = StringRes.get("open_in_browser"))
            }
        }

        if(trainData.stops.size > 1) {

            HorizontalDivider( modifier = Modifier.padding( vertical = 12.dp ) )
            Text(StringRes.get("next_stops"))
            TrainStopList(
                stops = trainData.stops,
                delay = trainData.delayMinutes,
                stationType = trainData.currentStationType
            )
        }
    }
}
