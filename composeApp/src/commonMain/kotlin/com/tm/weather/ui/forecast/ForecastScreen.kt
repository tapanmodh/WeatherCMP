package com.tm.weather.ui.forecast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tm.weather.data.models.Item0
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import weathercmp.composeapp.generated.resources.Res
import weathercmp.composeapp.generated.resources.ic_cloud
import weathercmp.composeapp.generated.resources.ic_rain
import weathercmp.composeapp.generated.resources.ic_sun

@Composable
fun ForecastScreen(navController: NavController) {

    val factory = rememberLocationTrackerFactory(LocationTrackerAccuracy.Best)
    val locationTracker = remember { factory.createLocationTracker() }
    BindLocationTrackerEffect(locationTracker)
    val viewModel = remember { ForecastViewModel(locationTracker) }

    val state = viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getForecast()
    }
    Column(
        modifier = Modifier.fillMaxSize().background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF7FD4FF),
                    Color(0xFF4A90E2)
                )
            )
        ).systemBarsPadding(),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
        }
        when (val forecastState = state.value) {
            is ForecastState.Loading -> {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Text(text = "Loading...", color = Color.White)
                }
            }

            is ForecastState.Data -> {

                val dailyData = forecastState.dailyData
                val weeklyData = forecastState.weeklyData
                ForecastScreenContent(dailyData, weeklyData)
            }

            is ForecastState.Error -> {
                Text(text = "Error occurred")
            }
        }
    }
}

@Composable
fun ColumnScope.ForecastScreenContent(dailyData: List<Item0>, weeklyData: List<Item0>) {
    Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "Daily Forecast", modifier = Modifier.align(Alignment.CenterStart), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = dailyData[0].dt_txt.split(" ")[0], modifier = Modifier.align(Alignment.CenterEnd), color = Color.White)
    }
    LazyRow {
        items(dailyData) { data ->
            ForecastRowItem(data)
        }
    }
    Spacer(modifier = Modifier.size(16.dp))
    Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "Next Forecast", modifier = Modifier.align(Alignment.CenterStart), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = dailyData[0].dt_txt.split(" ")[0], modifier = Modifier.align(Alignment.CenterEnd), color = Color.White)
    }
    LazyColumn {
        items(weeklyData) { data ->
            ForecastColumnItem(data)
        }
    }
    Spacer(modifier = Modifier.size(16.dp))
}

@Composable
fun ForecastRowItem(
    item: Item0
) {
    Column (
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp).height(155.dp)
    ) {
        Text(text = "${item.main.temp?.toInt()}°C", color = Color.White)
        Spacer(modifier = Modifier.size(8.dp))
        Image(painter = painterResource(getImage(item.weather.getOrNull(0)?.main?:"")), contentDescription = null, modifier = Modifier.size(60.dp))
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = item.dt_txt.split(" ")[1].removeSuffix(":00"), color = Color.White)
    }
}

fun getImage(data: String): DrawableResource {
    return if(data.lowercase().contains("rain")) {
        Res.drawable.ic_cloud
    } else if(data.lowercase().contains("cloud")) {
        Res.drawable.ic_cloud
    } else {
        Res.drawable.ic_cloud
    }
}

@Composable
fun ForecastColumnItem(
    item: Item0
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = item.dt_txt.split(" ")[0].drop(5), color = Color.White)
        Spacer(modifier = Modifier.size(8.dp))
        Image(painter = painterResource(getImage(item.weather.getOrNull(0)?.main?:"")), contentDescription = null, modifier = Modifier.size(60.dp))
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = "${item.main.temp?.toInt()}°C", color = Color.White)
    }
}