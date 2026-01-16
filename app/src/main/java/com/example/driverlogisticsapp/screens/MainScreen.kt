package com.example.driverlogisticsapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.WorkManager
import com.example.driverlogisticsapp.DeliveryViewModel
import com.example.driverlogisticsapp.database.DeliveryEntity
import androidx.work.WorkInfo
import com.example.driverlogisticsapp.queueDelivery

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(deliveryViewModel: DeliveryViewModel){
    val deliveryList: List<DeliveryEntity> by deliveryViewModel.deliveryList.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var deliveryInfo by remember {
        mutableStateOf(
            DeliveryEntity(
                101,
                "Bulto de Fertilizante NPK",
                "Vereda La Clarita, Finca El Recuerdo, Amalfi, Antioquia"
            )
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {Text(text = "Top bar")}
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(4.dp)
        ){
            items(items = deliveryList){ deliveryItem ->
                val workInfos by WorkManager.getInstance(LocalContext.current)
                    .getWorkInfosByTagFlow (deliveryItem.id.toString())
                    .collectAsState(emptyList())
                val info = workInfos.lastOrNull()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 15.dp)
                        .drawBehind {
                            val strokeWidth = 2.dp.toPx()
                            val y = size.height - strokeWidth / 2
                            drawLine(
                                color = Color.Black,
                                start = Offset(50f, y),
                                end = Offset(size.width-50, y),
                                strokeWidth = strokeWidth
                            )
                        }
                        .clickable(
                            onClick = {
                                deliveryInfo = deliveryItem
                                showDialog = true
                            }
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(top = 5.dp, bottom = 5.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = deliveryItem.id.toString(),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                fontSize = 24.sp,
                                fontStyle = FontStyle.Italic,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxSize()
                        )
                        Text(
                            text = "Dirección: ${deliveryItem.address}",
                            style = TextStyle(
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxSize()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val style = TextStyle(
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Estado: ",
                                style = style,
                                modifier = Modifier
                            )
                            Text(
                                text = if (deliveryItem.status) "Entregado"
                                else "Pendiente",
                                color = if(deliveryItem.status) Color.Green else Color.Red,
                                style = style,
                                modifier = Modifier
                            )
                        }

                        val style = TextStyle(
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                        val modifier = Modifier.fillMaxWidth()
                        if (deliveryItem.status)
                        when (info?.state){
                            WorkInfo.State.ENQUEUED -> Text(
                                "Esperando conexión",
                                style = style,
                                modifier = modifier
                            )
                            WorkInfo.State.RUNNING -> Text(
                                "Enviando confirmación...",
                                style = style,
                                modifier = modifier
                            )
                            WorkInfo.State.SUCCEEDED -> Text(
                                "Entrega confirmada con el servidor",
                                style = style,
                                modifier = modifier
                            )
                            WorkInfo.State.FAILED -> Text(
                                "Error al enviar confirmación",
                                style = style,
                                modifier = modifier
                            )
                            else -> {}
                        }
                    }
                }
            }
        }
        if ( deliveryViewModel.isLoading ){
            Dialog(
                onDismissRequest = {}
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Cargando envíos...",
                            style = TextStyle(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        LinearProgressIndicator(
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }
        }
    }

    if (showDialog){
        InfoScreen(
            info = deliveryInfo,
            setShowDialog = { newValue -> showDialog = newValue },
            viewModel = deliveryViewModel
        )
    }
}



@Composable
fun InfoScreen(
    info: DeliveryEntity,
    viewModel: DeliveryViewModel,
    setShowDialog: (Boolean) -> Unit
){
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = { setShowDialog(false)},
        title = { Text(info.name) },
        text = { Text("Dirección: ${info.address}") },
        confirmButton = {
            Button(
                onClick = {
                    queueDelivery(context, info.id)
                    viewModel.updateStatus(info.id, true)
                    setShowDialog(false)
                },
                enabled = !info.status
            ) {
                Text("Marcar como entregado")
            }
        }
    )
}