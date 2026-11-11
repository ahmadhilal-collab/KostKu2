package com.kostku.app.ui.screens.room

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Bed
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kostku.app.R
import com.kostku.app.data.local.RoomEntity
import com.kostku.app.ui.components.entrance
import com.kostku.app.ui.theme.Green50
import com.kostku.app.ui.theme.Green700
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900
import com.kostku.app.ui.theme.Line
import com.kostku.app.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KatalogKamarScreen(
    rooms: List<RoomEntity>,
    onBack: () -> Unit,
    onRoomClick: (Int) -> Unit
) {
    var selectedFloor by remember { mutableIntStateOf(0) } // 0 = Semua, 1 = Lantai 1, 2 = Lantai 2

    val filtered = when (selectedFloor) {
        1 -> rooms.filter { it.floor == 1 }
        2 -> rooms.filter { it.floor == 2 }
        else -> rooms
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Katalog Kamar New Afra Kost", style = MaterialTheme.typography.titleMedium, color = Ink900) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Kembali", tint = Ink900)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text("Pilih kamar sesuai kebutuhanmu", style = MaterialTheme.typography.bodySmall, color = Ink600, modifier = Modifier.entrance(0))
            Spacer(Modifier.height(12.dp))

            // Filter Tabs
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.entrance(1)) {
                CatalogFilterChip("Semua", selected = selectedFloor == 0, onClick = { selectedFloor = 0 }, modifier = Modifier.weight(1f))
                CatalogFilterChip("Lantai 1", selected = selectedFloor == 1, onClick = { selectedFloor = 1 }, modifier = Modifier.weight(1f))
                CatalogFilterChip("Lantai 2", selected = selectedFloor == 2, onClick = { selectedFloor = 2 }, modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filtered, key = { it.id }) { room ->
                    CatalogCardItem(
                        room = room,
                        onClick = { onRoomClick(room.number) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CatalogFilterChip(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) Green700 else White,
        border = BorderStroke(1.dp, if (selected) Green700 else Line),
        modifier = modifier.height(38.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = if (selected) White else Ink900)
        }
    }
}

@Composable
private fun CatalogCardItem(
    room: RoomEntity,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = White,
        border = BorderStroke(1.dp, Line),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Room Image Carousel Placeholder (User Room Photo)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Image(
                    painter = painterResource(if (room.number % 2 == 0) R.drawable.room_photo_2 else R.drawable.room_photo_1),
                    contentDescription = "Kamar ${room.number}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Green50,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                ) {
                    Text("TERSEDIA", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Green700, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), fontSize = 10.sp)
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = White.copy(alpha = 0.85f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                ) {
                    Text("📷 1 / 4", style = MaterialTheme.typography.labelSmall, color = Ink900, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp)
                }
            }

            Column(Modifier.padding(14.dp)) {
                Text("Kamar ${room.number} - Lantai ${room.floor}", style = MaterialTheme.typography.titleMedium, color = Ink900)
                Spacer(Modifier.height(2.dp))
                Text("Rp 800.000 / Bulan (All-In)", style = MaterialTheme.typography.titleSmall, color = Green700)

                Spacer(Modifier.height(12.dp))

                // Facility Icons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    FacilityItem(Icons.Outlined.Bed, "Kasur")
                    FacilityItem(Icons.Outlined.Wifi, "Wi-Fi")
                    FacilityItem(Icons.Outlined.ElectricBolt, "Listrik")
                    FacilityItem(Icons.Outlined.WaterDrop, "Air")
                }
            }
        }
    }
}

@Composable
private fun FacilityItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = Green700, modifier = Modifier.size(20.dp))
        Spacer(Modifier.height(2.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = Ink600, fontSize = 10.sp)
    }
}
