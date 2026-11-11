package com.kostku.app.ui.screens.room

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Bed
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kostku.app.R
import com.kostku.app.data.local.RoomStatus
import com.kostku.app.ui.components.PrimaryButton
import com.kostku.app.ui.components.entrance
import com.kostku.app.ui.theme.Canvas
import com.kostku.app.ui.theme.Danger
import com.kostku.app.ui.theme.DangerBg
import com.kostku.app.ui.theme.Green50
import com.kostku.app.ui.theme.Green700
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900
import com.kostku.app.ui.theme.Line
import com.kostku.app.ui.theme.Warning
import com.kostku.app.ui.theme.WarningBg
import com.kostku.app.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailKamarScreen(
    roomNumber: Int,
    onBack: () -> Unit,
    onConfirmPayment: (Int) -> Unit,
    onAddTenantForRoom: (Int) -> Unit = {},
    onStatusChanged: (Int, RoomStatus) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    var photoCount by remember { mutableStateOf(4) }
    var currentStatus by remember {
        mutableStateOf(
            if (roomNumber == 103) RoomStatus.MAINTENANCE else RoomStatus.OCCUPIED
        )
    }
    var expandedStatusMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Kamar $roomNumber (Lantai ${if (roomNumber < 200) 1 else 2})", style = MaterialTheme.typography.titleMedium, color = Ink900) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Kembali", tint = Ink900)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        bottomBar = {
            Surface(
                color = White,
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(Modifier.padding(16.dp)) {
                    PrimaryButton(
                        text = "CATAT PEMBAYARAN TUNAI (Rp 800.000)",
                        onClick = { onConfirmPayment(roomNumber) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Status Tag Badge & Change Status Dropdown Trigger
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .entrance(0),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val (badgeLabel, bgCol, textCol) = when (currentStatus) {
                    RoomStatus.OCCUPIED -> Triple("Terisi • Lunas", Green50, Green700)
                    RoomStatus.EMPTY -> Triple("Kosong / Siap Huni", Canvas, Ink600)
                    RoomStatus.MAINTENANCE -> Triple("Perbaikan • Nonaktif", WarningBg, Warning)
                }

                Surface(shape = RoundedCornerShape(8.dp), color = bgCol) {
                    Text(
                        text = badgeLabel,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = textCol,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Box {
                    Surface(
                        onClick = { expandedStatusMenu = true },
                        shape = RoundedCornerShape(8.dp),
                        color = White,
                        border = BorderStroke(1.dp, Line)
                    ) {
                        Text(
                            text = "Ubah Status Kamar ∨",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Green700,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = expandedStatusMenu,
                        onDismissRequest = { expandedStatusMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("● Ubah ke KOSONG (Siap Huni)", color = Ink600) },
                            onClick = {
                                currentStatus = RoomStatus.EMPTY
                                expandedStatusMenu = false
                                onStatusChanged(roomNumber, RoomStatus.EMPTY)
                                Toast.makeText(context, "Status Kamar $roomNumber diubah menjadi KOSONG!", Toast.LENGTH_SHORT).show()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("● Ubah ke TERISI (Lunas)", color = Green700) },
                            onClick = {
                                currentStatus = RoomStatus.OCCUPIED
                                expandedStatusMenu = false
                                onStatusChanged(roomNumber, RoomStatus.OCCUPIED)
                                Toast.makeText(context, "Status Kamar $roomNumber diubah menjadi TERISI!", Toast.LENGTH_SHORT).show()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("● Ubah ke PERBAIKAN", color = Warning) },
                            onClick = {
                                currentStatus = RoomStatus.MAINTENANCE
                                expandedStatusMenu = false
                                onStatusChanged(roomNumber, RoomStatus.MAINTENANCE)
                                Toast.makeText(context, "Status Kamar $roomNumber diubah menjadi PERBAIKAN!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Action Button: "+ Tambah Data Penghuni" for this specific room!
            OutlinedButton(
                onClick = { onAddTenantForRoom(roomNumber) },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.2.dp, Green700),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .entrance(1)
            ) {
                Icon(Icons.Outlined.PersonAdd, contentDescription = null, tint = Green700, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("+ Tambah Data Penghuni Kamar $roomNumber", style = MaterialTheme.typography.labelLarge, color = Green700)
            }

            Spacer(Modifier.height(16.dp))

            // Photo Carousel Section Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .entrance(2),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Foto kamar", style = MaterialTheme.typography.titleSmall, color = Ink900)
                Text("$photoCount foto", style = MaterialTheme.typography.bodySmall, color = Ink600)
            }

            Spacer(Modifier.height(10.dp))

            // Main Room Photo Display (User Room Photo 1)
            Surface(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Line),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .entrance(3)
            ) {
                Image(
                    painter = painterResource(if (roomNumber % 2 == 0) R.drawable.room_photo_2 else R.drawable.room_photo_1),
                    contentDescription = "Foto Kamar $roomNumber",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.height(12.dp))

            // "+ Tambah / Ganti Foto Kamar" Button
            OutlinedButton(
                onClick = {
                    photoCount++
                    Toast.makeText(context, "Foto Kamar $roomNumber berhasil ditambah!", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Green700),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .entrance(4)
            ) {
                Text("+ Tambah / Ganti Foto Kamar", style = MaterialTheme.typography.labelMedium, color = Green700)
            }

            Spacer(Modifier.height(24.dp))

            // Facility List Section
            Text("Fasilitas All-In", style = MaterialTheme.typography.titleSmall, color = Ink900, modifier = Modifier.entrance(5))
            Spacer(Modifier.height(10.dp))

            val facilities = listOf(
                Pair(Icons.Outlined.Bed, "Kasur Springbed & Bantal"),
                Pair(Icons.Outlined.AcUnit, "AC / Kipas Angin"),
                Pair(Icons.Outlined.Wifi, "Wi-Fi Akses Cepat"),
                Pair(Icons.Outlined.ElectricBolt, "Listrik Termasuk (All-In)"),
                Pair(Icons.Outlined.WaterDrop, "Air Bersih Termasuk")
            )

            facilities.forEachIndexed { idx, (icon, text) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .entrance(6 + idx)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Green50, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = null, tint = Green700, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(text, style = MaterialTheme.typography.bodyMedium, color = Ink900)
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
