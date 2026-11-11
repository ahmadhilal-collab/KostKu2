package com.kostku.app.ui.screens.home

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Announcement
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Bed
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MeetingRoom
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.ViewModule
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kostku.app.data.local.PaymentEntity
import com.kostku.app.data.local.RoomEntity
import com.kostku.app.data.local.RoomStatus
import com.kostku.app.data.local.Session
import com.kostku.app.data.repository.RoomSummary
import com.kostku.app.ui.components.PrimaryButton
import com.kostku.app.ui.components.SecondaryButton
import com.kostku.app.ui.components.entrance
import com.kostku.app.ui.screens.repair.AdminPerbaikanScreen
import com.kostku.app.ui.screens.tenant.DataPenghuniContent
import com.kostku.app.ui.screens.tenant.ProfilInfoContent
import com.kostku.app.ui.theme.Canvas
import com.kostku.app.ui.theme.Danger
import com.kostku.app.ui.theme.DangerBg
import com.kostku.app.ui.theme.Green100
import com.kostku.app.ui.theme.Green50
import com.kostku.app.ui.theme.Green700
import com.kostku.app.ui.theme.Green900
import com.kostku.app.ui.theme.Ink400
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900
import com.kostku.app.ui.theme.Line
import com.kostku.app.ui.theme.Warning
import com.kostku.app.ui.theme.WarningBg
import com.kostku.app.ui.theme.White
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    session: Session,
    summary: RoomSummary,
    roomsFlow: Flow<List<RoomEntity>>,
    onLogout: () -> Unit,
    onNavigateToDenah: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToKatalog: () -> Unit,
    onNavigateToDataPenghuni: () -> Unit,
    onNavigateToBuatPengumuman: () -> Unit,
    onNavigateToNota: (Int) -> Unit,
    onNavigateToDetailLaporan: (String) -> Unit,
    onRecordPayment: suspend (Int) -> PaymentEntity?
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val rooms by roomsFlow.collectAsState(initial = emptyList())
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var paymentConfirmRoomNumber by remember { mutableStateOf<Int?>(null) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        bottomBar = {
            AdminBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(White)
        ) {
            when (selectedTab) {
                0 -> AdminDashboardTab(
                    session = session,
                    summary = summary,
                    rooms = rooms,
                    onNavigateToDenah = onNavigateToDenah,
                    onNavigateToBuatPengumuman = onNavigateToBuatPengumuman,
                    onNavigateToDataPenghuni = onNavigateToDataPenghuni,
                    onNavigateToKatalog = onNavigateToKatalog,
                    onNavigateToNota = { onNavigateToNota(101) },
                    onRoomClick = { room ->
                        if (room.status == RoomStatus.OCCUPIED && (room.number == 102 || room.number == 104 || room.number == 109)) {
                            paymentConfirmRoomNumber = room.number
                        } else {
                            onNavigateToDetail(room.number)
                        }
                    },
                    onWaClick = { room ->
                        val phone = "081234567890"
                        val message = "Halo, kami mengingatkan tagihan sewa New Afra Kost Kamar ${room.number} periode November 2026 sebesar Rp800.000 belum dibayar. Terima kasih - Pak Abbas."
                        val url = "https://api.whatsapp.com/send?phone=$phone&text=${Uri.encode(message)}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Aplikasi WhatsApp tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
                1 -> DenahKamarContent(
                    rooms = rooms,
                    onRoomClick = { onNavigateToDetail(it.number) }
                )
                2 -> AdminPerbaikanScreen(
                    onBack = { selectedTab = 0 },
                    onOpenDetail = { reportId -> onNavigateToDetailLaporan(reportId) }
                )
                3 -> ProfilInfoContent(
                    onLogout = onLogout
                )
            }
        }
    }

    // Modal Bottom Sheet Konfirmasi Pembayaran Tunai
    if (paymentConfirmRoomNumber != null) {
        ModalBottomSheet(
            onDismissRequest = { paymentConfirmRoomNumber = null },
            sheetState = sheetState,
            containerColor = White
        ) {
            val roomNo = paymentConfirmRoomNumber!!
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text("Halo, Pak Abbas", style = MaterialTheme.typography.bodySmall, color = Ink600)
                Text("New Afra Kost • Jl. Perintis Kemerdekaan 4", style = MaterialTheme.typography.labelSmall, color = Ink400)
                Spacer(Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = White,
                    border = BorderStroke(1.dp, Line)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Konfirmasi Pembayaran Tunai", style = MaterialTheme.typography.titleMedium, color = Ink900)
                        Spacer(Modifier.height(12.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Kamar $roomNo", style = MaterialTheme.typography.titleSmall, color = Ink900)
                            Surface(shape = RoundedCornerShape(6.dp), color = DangerBg) {
                                Text("BELUM BAYAR", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Danger, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Text("Periode: November 2026", style = MaterialTheme.typography.bodySmall, color = Ink600)
                        Spacer(Modifier.height(12.dp))
                        Text("Total Pembayaran All-In", style = MaterialTheme.typography.bodySmall, color = Ink600)
                        Text("Rp 800.000", style = MaterialTheme.typography.headlineMedium, color = Green700)
                        Spacer(Modifier.height(4.dp))
                        Text("Terbilang: Delapan Ratus Ribu Rupiah.", style = MaterialTheme.typography.bodySmall, color = Ink600)
                        Spacer(Modifier.height(10.dp))
                        Text("Pastikan uang tunai sudah Anda terima sebelum konfirmasi.", style = MaterialTheme.typography.labelSmall, color = Ink400)
                    }
                }

                Spacer(Modifier.height(20.dp))
                PrimaryButton(
                    text = "✓ YA, SUDAH TERIMA UANG",
                    onClick = {
                        coroutineScope.launch {
                            onRecordPayment(roomNo)
                            Toast.makeText(context, "Pembayaran Kamar $roomNo Berhasil Dikonfirmasi!", Toast.LENGTH_SHORT).show()
                            paymentConfirmRoomNumber = null
                            onNavigateToNota(roomNo)
                        }
                    }
                )
                Spacer(Modifier.height(10.dp))
                SecondaryButton(
                    text = "Batal",
                    onClick = { paymentConfirmRoomNumber = null }
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun AdminDashboardTab(
    session: Session,
    summary: RoomSummary,
    rooms: List<RoomEntity>,
    onNavigateToDenah: () -> Unit,
    onNavigateToBuatPengumuman: () -> Unit,
    onNavigateToDataPenghuni: () -> Unit,
    onNavigateToKatalog: () -> Unit,
    onNavigateToNota: () -> Unit,
    onRoomClick: (RoomEntity) -> Unit,
    onWaClick: (RoomEntity) -> Unit
) {
    var selectedFloor by remember { mutableIntStateOf(1) }
    var showFtueGuide by remember { mutableStateOf(true) }
    val filteredRooms = rooms.filter { it.floor == selectedFloor }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp)
    ) {
        item {
            // Header: Greeting + Home Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .entrance(0),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Halo, ${session.name}", style = MaterialTheme.typography.headlineMedium, color = Ink900)
                    Text("New Afra Kost • Jl. Perintis Kemerdekaan 4", style = MaterialTheme.typography.bodySmall, color = Ink600)
                }
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Green50, CircleShape)
                        .border(1.dp, Line, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Home, contentDescription = null, tint = Green700, modifier = Modifier.size(22.dp))
                }
            }

            Spacer(Modifier.height(16.dp))

            // First-Time User Experience (FTUE) Welcome Guide Banner
            if (showFtueGuide) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Green50,
                    border = BorderStroke(1.dp, Line),
                    modifier = Modifier
                        .fillMaxWidth()
                        .entrance(1)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Star, contentDescription = null, tint = Green700, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Panduan Pengguna Baru KostKu", style = MaterialTheme.typography.titleSmall, color = Green700)
                            }
                            IconButton(
                                onClick = { showFtueGuide = false },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Outlined.Close, contentDescription = "Tutup", tint = Ink400, modifier = Modifier.size(16.dp))
                            }
                        }

                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Gunakan tombol menu di bawah untuk mengelola denah, pengumuman, data penghuni, dan katalog. Pilih tab 'Perbaikan' untuk memantau keluhan fasilitas secara langsung.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Ink900,
                            lineHeight = 18.sp
                        )

                        Spacer(Modifier.height(10.dp))
                        Surface(
                            onClick = { showFtueGuide = false },
                            shape = RoundedCornerShape(8.dp),
                            color = Green700
                        ) {
                            Text("Paham & Tutup Panduan", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = White, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            // Akses fitur Section Title
            Text("Akses fitur", style = MaterialTheme.typography.titleMedium, color = Ink900, modifier = Modifier.entrance(2))
            Spacer(Modifier.height(12.dp))

            // Akses Fitur Layout (Matching Screenshot 1: Icon Box TOP, Title Text BOTTOM!)
            Column(
                modifier = Modifier.fillMaxWidth().entrance(3),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AdminFeatureCardVertical("Denah kamar", Icons.Outlined.Bed, onNavigateToDenah, Modifier.weight(1f))
                    AdminFeatureCardVertical("Pengumuman", Icons.AutoMirrored.Outlined.Announcement, onNavigateToBuatPengumuman, Modifier.weight(1f))
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AdminFeatureCardVertical("Data penghuni", Icons.Outlined.People, onNavigateToDataPenghuni, Modifier.weight(1f))
                    AdminFeatureCardVertical("Katalog kamar", Icons.Outlined.ViewModule, onNavigateToKatalog, Modifier.weight(1f))
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AdminFeatureCardVertical("Nota pembayaran", Icons.AutoMirrored.Outlined.ReceiptLong, onNavigateToNota, Modifier.weight(1f))
                    Spacer(Modifier.weight(1f))
                }
            }

            Spacer(Modifier.height(20.dp))

            // Summary Stat Cards
            Row(Modifier.fillMaxWidth().entrance(4), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("34", "Kamar Terisi", Modifier.weight(1f))
                StatCard("2", "Kamar Non-Aktif/Perbaikan", Modifier.weight(1f))
            }

            Spacer(Modifier.height(20.dp))

            // Floor Selector Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .entrance(5),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FloorChip("Lantai 1 (16 Kamar)", selected = selectedFloor == 1, onClick = { selectedFloor = 1 }, modifier = Modifier.weight(1f))
                FloorChip("Lantai 2 (20 Kamar)", selected = selectedFloor == 2, onClick = { selectedFloor = 2 }, modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))
        }

        // Room Cards List
        items(filteredRooms, key = { it.id }) { room ->
            RoomCardItem(
                room = room,
                onClick = { onRoomClick(room) },
                onWaClick = { onWaClick(room) }
            )
            Spacer(Modifier.height(10.dp))
        }
    }
}

// Vertical Card Layout Matching Screenshot 1 Exactly!
@Composable
private fun AdminFeatureCardVertical(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = White,
        border = BorderStroke(1.dp, Line),
        shadowElevation = 1.dp,
        modifier = modifier.height(96.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Green50, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Green700, modifier = Modifier.size(20.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Ink900,
                fontSize = 12.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = White,
        border = BorderStroke(1.dp, Line),
        modifier = modifier
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(value, style = MaterialTheme.typography.headlineLarge, color = Ink900)
            Spacer(Modifier.height(2.dp))
            Text(label, style = MaterialTheme.typography.bodySmall, color = Ink600, fontSize = 11.sp)
        }
    }
}

@Composable
private fun FloorChip(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) Green700 else White,
        border = BorderStroke(1.dp, if (selected) Green700 else Line),
        modifier = modifier.height(42.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = if (selected) White else Ink900, fontSize = 12.sp)
        }
    }
}

@Composable
private fun RoomCardItem(
    room: RoomEntity,
    onClick: () -> Unit,
    onWaClick: () -> Unit
) {
    val formatRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    val priceText = formatRupiah.format(room.monthlyRate).replace(",00", "")

    val isUnpaid = room.number == 102 || room.number == 104 || room.number == 109 || room.number == 114

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = White,
        border = BorderStroke(1.dp, Line),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Kamar ${room.number}", style = MaterialTheme.typography.titleMedium, color = Ink900)
                    Text(
                        if (room.number == 101) "Siti Aminah" else if (room.number == 102) "Andi" else if (room.status == RoomStatus.EMPTY) "Belum ada penghuni" else "Penghuni Kamar ${room.number}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Ink600
                    )
                }

                // Status Badge Color-Coding
                val (badgeText, bgCol, textCol) = when {
                    room.status == RoomStatus.MAINTENANCE -> Triple("PERBAIKAN", WarningBg, Warning)
                    room.status == RoomStatus.EMPTY -> Triple("KOSONG", Canvas, Ink600)
                    isUnpaid -> Triple("BELUM BAYAR", DangerBg, Danger)
                    else -> Triple("LUNAS", Green50, Green700)
                }
                Surface(shape = RoundedCornerShape(6.dp), color = bgCol) {
                    Text(badgeText, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = textCol, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(priceText, style = MaterialTheme.typography.titleSmall, color = Green700)

            if (room.status == RoomStatus.MAINTENANCE) {
                Spacer(Modifier.height(4.dp))
                Text(room.maintenanceNote ?: "Perbaikan fasilitas • Kamar non-aktif", style = MaterialTheme.typography.bodySmall, color = Warning)
            } else {
                Text(if (isUnpaid) "Jatuh tempo: 10 November 2026" else "Tunai • 1 November 2026", style = MaterialTheme.typography.labelSmall, color = if (isUnpaid) Danger else Ink400)
            }

            // WA Button if unpaid
            if (isUnpaid) {
                Spacer(Modifier.height(10.dp))
                OutlinedButton(
                    onClick = onWaClick,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Green700),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("Ingatkan via WA", style = MaterialTheme.typography.labelSmall, color = Green700)
                }
            }
        }
    }
}

// Architectural Denah Layout Exactly Divided into 3 Equal Columns & Middle Bottom Rooms!
@Composable
fun DenahKamarContent(
    rooms: List<RoomEntity>,
    onRoomClick: (RoomEntity) -> Unit
) {
    var selectedFloor by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text("Denah New Afra Kost", style = MaterialTheme.typography.headlineMedium, color = Ink900)
        Text("Pantau hunian dan pembayaran setiap kamar.", style = MaterialTheme.typography.bodySmall, color = Ink600)
        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            FloorChip("Lantai 1 (20 Kamar)", selected = selectedFloor == 1, onClick = { selectedFloor = 1 }, modifier = Modifier.weight(1f))
            FloorChip("Lantai 2 (20 Kamar)", selected = selectedFloor == 2, onClick = { selectedFloor = 2 }, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Lantai $selectedFloor", style = MaterialTheme.typography.titleMedium, color = Ink900)
            Text("20 kamar • ${if (selectedFloor == 1) "101–120" else "201–220"}", style = MaterialTheme.typography.bodySmall, color = Ink600)
        }

        Spacer(Modifier.height(12.dp))

        // Architectural Blueprint Container Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = White,
            border = BorderStroke(1.dp, Line),
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                if (selectedFloor == 1) {
                    // LANTAI 1 BLUEPRINT LAYOUT (3 EQUAL COLUMNS + MIDDLE BOTTOM ROOMS 101 & 102)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Left Column (6 rooms: 120, 119, 118, 117, 116, 115)
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            BlueprintTile(120, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(119, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(118, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(117, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(116, RoomStatus.OCCUPIED, onRoomClick)
                            BlueprintTile(115, RoomStatus.OCCUPIED, onRoomClick)
                        }

                        // Middle Column (6 rooms: 114, 113, 112, 111, 110, 109)
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            BlueprintTile(114, RoomStatus.OCCUPIED, onRoomClick, isUnpaid = true)
                            BlueprintTile(113, RoomStatus.OCCUPIED, onRoomClick)
                            BlueprintTile(112, RoomStatus.OCCUPIED, onRoomClick)
                            BlueprintTile(111, RoomStatus.OCCUPIED, onRoomClick)
                            BlueprintTile(110, RoomStatus.OCCUPIED, onRoomClick)
                            BlueprintTile(109, RoomStatus.OCCUPIED, onRoomClick, isUnpaid = true)
                        }

                        // Right Column (6 rooms: 108, 107, 106, 105, 104, 103)
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            BlueprintTile(108, RoomStatus.OCCUPIED, onRoomClick)
                            BlueprintTile(107, RoomStatus.OCCUPIED, onRoomClick)
                            BlueprintTile(106, RoomStatus.OCCUPIED, onRoomClick)
                            BlueprintTile(105, RoomStatus.OCCUPIED, onRoomClick)
                            BlueprintTile(104, RoomStatus.OCCUPIED, onRoomClick, isUnpaid = true)
                            BlueprintTile(103, RoomStatus.MAINTENANCE, onRoomClick)
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // Middle Bottom Row for Rooms 101 and 102 (Centered!)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = Modifier.width(220.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            BlueprintTile(101, RoomStatus.OCCUPIED, onRoomClick, modifier = Modifier.weight(1f))
                            BlueprintTile(102, RoomStatus.OCCUPIED, onRoomClick, isUnpaid = true, modifier = Modifier.weight(1f))
                        }
                    }

                } else {
                    // LANTAI 2 BLUEPRINT LAYOUT (3 EQUAL COLUMNS + MIDDLE BOTTOM ROOMS 201 & 214)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Left Column (6 rooms: 207..202)
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            BlueprintTile(207, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(206, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(205, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(204, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(203, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(202, RoomStatus.EMPTY, onRoomClick)
                        }

                        // Middle Column (6 rooms: 213..208)
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            BlueprintTile(213, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(212, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(211, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(210, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(209, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(208, RoomStatus.EMPTY, onRoomClick)
                        }

                        // Right Column (6 rooms: 220..215)
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            BlueprintTile(220, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(219, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(218, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(217, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(216, RoomStatus.EMPTY, onRoomClick)
                            BlueprintTile(215, RoomStatus.EMPTY, onRoomClick)
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // Middle Bottom Row for Rooms 201 and 214 (Centered!)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = Modifier.width(220.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            BlueprintTile(201, RoomStatus.EMPTY, onRoomClick, modifier = Modifier.weight(1f))
                            BlueprintTile(214, RoomStatus.EMPTY, onRoomClick, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Status Legend Box
        Surface(shape = RoundedCornerShape(12.dp), color = White, border = BorderStroke(1.dp, Line)) {
            Column(Modifier.padding(14.dp)) {
                Text("Keterangan status denah", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = Ink900)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth()) {
                    Text("● Terisi • Lunas", color = Green700, style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1f))
                    Text("● Terisi • Belum bayar", color = Danger, style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1f))
                }
                Spacer(Modifier.height(6.dp))
                Row(Modifier.fillMaxWidth()) {
                    Text("● Perbaikan • Nonaktif", color = Warning, style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1f))
                    Text("● Kosong • Siap huni", color = Ink600, style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1f))
                }

                Spacer(Modifier.height(10.dp))
                Text(
                    text = if (selectedFloor == 1) "11 lunas • 4 belum bayar • 1 perbaikan. Data kamar 117–120 belum tersedia." else "Data penghuni dan status kamar 201–220 belum tersedia.",
                    style = MaterialTheme.typography.labelSmall,
                    color = Ink400,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun BlueprintTile(
    number: Int,
    status: RoomStatus,
    onRoomClick: (RoomEntity) -> Unit,
    isUnpaid: Boolean = false,
    modifier: Modifier = Modifier
) {
    val (bgCol, borderCol, textCol, statusText) = when {
        status == RoomStatus.MAINTENANCE -> Quadruple(WarningBg, Warning, Warning, "Perbaikan")
        status == RoomStatus.EMPTY -> Quadruple(Canvas, Line, Ink600, "Kosong")
        isUnpaid -> Quadruple(DangerBg, Danger, Danger, "Belum Bayar")
        else -> Quadruple(Green50, Green700, Green700, "Lunas")
    }

    Surface(
        onClick = { onRoomClick(RoomEntity(number = number, floor = if (number < 200) 1 else 2, status = status)) },
        shape = RoundedCornerShape(8.dp),
        color = bgCol,
        border = BorderStroke(1.dp, borderCol),
        modifier = modifier.height(56.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(2.dp)
        ) {
            Text(number.toString(), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = textCol, fontSize = 14.sp)
            Text(statusText, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = textCol, fontSize = 10.sp, maxLines = 1)
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
private fun AdminBottomNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = White,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple("Beranda", Icons.Outlined.Home, 0),
            Triple("Kelola Kamar", Icons.Outlined.MeetingRoom, 1),
            Triple("Perbaikan", Icons.Outlined.Build, 2),
            Triple("Profil & Info", Icons.Outlined.Info, 3)
        )
        items.forEach { (label, icon, index) ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Green700,
                    selectedTextColor = Green700,
                    indicatorColor = Green50,
                    unselectedIconColor = Ink400,
                    unselectedTextColor = Ink400
                )
            )
        }
    }
}
