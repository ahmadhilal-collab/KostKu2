package com.kostku.app.ui.screens.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kostku.app.ui.components.entrance
import com.kostku.app.ui.theme.Danger
import com.kostku.app.ui.theme.DangerBg
import com.kostku.app.ui.theme.Green50
import com.kostku.app.ui.theme.Green700
import com.kostku.app.ui.theme.Ink400
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900
import com.kostku.app.ui.theme.Line
import com.kostku.app.ui.theme.White

data class AdminReceiptItem(
    val id: String,
    val roomNumber: Int,
    val tenantName: String,
    val period: String,
    val amount: Long,
    val isPaid: Boolean,
    val dateStr: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PilihNotaScreen(
    onBack: () -> Unit,
    onOpenReceipt: (Int) -> Unit
) {
    var selectedRoomFilter by remember { mutableIntStateOf(0) } // 0 = Semua, or 101, 102, 104, 105...

    val allReceipts = remember {
        listOf(
            AdminReceiptItem("1", 101, "Siti Aminah", "November 2026", 800_000L, true, "1 November 2026"),
            AdminReceiptItem("2", 101, "Siti Aminah", "Oktober 2026", 800_000L, true, "5 Oktober 2026"),
            AdminReceiptItem("3", 102, "Andi", "November 2026", 800_000L, false, "Jatuh tempo 10 Nov"),
            AdminReceiptItem("4", 102, "Andi", "Oktober 2026", 800_000L, true, "5 Oktober 2026"),
            AdminReceiptItem("5", 102, "Andi", "September 2026", 800_000L, true, "5 September 2026"),
            AdminReceiptItem("6", 104, "Rizky Pratama", "November 2026", 800_000L, false, "Jatuh tempo 10 Nov"),
            AdminReceiptItem("7", 105, "Andi Saputra", "November 2026", 800_000L, true, "1 November 2026"),
            AdminReceiptItem("8", 107, "Rina Wulandari", "November 2026", 800_000L, true, "2 November 2026"),
            AdminReceiptItem("9", 108, "Agus Setiawan", "November 2026", 800_000L, true, "1 November 2026"),
            AdminReceiptItem("10", 110, "Fajar Hidayat", "November 2026", 800_000L, true, "1 November 2026")
        )
    }

    val filteredReceipts = if (selectedRoomFilter == 0) {
        allReceipts
    } else {
        allReceipts.filter { it.roomNumber == selectedRoomFilter }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pilih Nota Pembayaran Kost", style = MaterialTheme.typography.titleMedium, color = Ink900) },
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
            Text("Pilihan Nota & Histori Transaksi", style = MaterialTheme.typography.headlineMedium, color = Ink900, modifier = Modifier.entrance(0))
            Text("Pilih nomor kamar atau transaksi untuk melihat nota pembayaran digital resmi.", style = MaterialTheme.typography.bodySmall, color = Ink600, modifier = Modifier.entrance(1))

            Spacer(Modifier.height(16.dp))

            // Room Filter Chips Row
            val roomOptions = listOf(0, 101, 102, 104, 105, 107, 108, 110)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.entrance(2)
            ) {
                items(roomOptions) { roomNo ->
                    val label = if (roomNo == 0) "Semua Kamar" else "Kamar $roomNo"
                    val selected = selectedRoomFilter == roomNo
                    Surface(
                        onClick = { selectedRoomFilter = roomNo },
                        shape = RoundedCornerShape(10.dp),
                        color = if (selected) Green700 else White,
                        border = BorderStroke(1.dp, if (selected) Green700 else Line),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 14.dp)) {
                            Text(label, style = MaterialTheme.typography.labelSmall, color = if (selected) White else Ink900)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Payment Items List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredReceipts, key = { it.id }) { item ->
                    Surface(
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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.AutoMirrored.Outlined.ReceiptLong, contentDescription = null, tint = Green700, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Kamar ${item.roomNumber} - ${item.tenantName}", style = MaterialTheme.typography.titleSmall, color = Ink900)
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (item.isPaid) Green50 else DangerBg
                                ) {
                                    Text(
                                        text = if (item.isPaid) "LUNAS" else "BELUM BAYAR",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (item.isPaid) Green700 else Danger,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            Spacer(Modifier.height(6.dp))
                            Text("Periode: ${item.period} • Rp 800.000", style = MaterialTheme.typography.bodyMedium, color = Ink900)
                            Text(item.dateStr, style = MaterialTheme.typography.labelSmall, color = Ink400)

                            Spacer(Modifier.height(10.dp))

                            if (item.isPaid) {
                                Surface(
                                    onClick = { onOpenReceipt(item.roomNumber) },
                                    shape = RoundedCornerShape(8.dp),
                                    color = Green50,
                                    border = BorderStroke(1.dp, Line),
                                    modifier = Modifier.fillMaxWidth().height(36.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    ) {
                                        Text("📄 Lihat Nota Pembayaran Digital ›", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Green700)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
