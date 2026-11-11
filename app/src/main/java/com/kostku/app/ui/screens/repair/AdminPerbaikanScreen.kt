package com.kostku.app.ui.screens.repair

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.kostku.app.ui.theme.Warning
import com.kostku.app.ui.theme.WarningBg
import com.kostku.app.ui.theme.White

data class AdminRepairItem(
    val id: String,
    val code: String,
    val title: String,
    val tenantName: String,
    val roomNumber: Int,
    val body: String,
    val dateStr: String,
    val statusLabel: String, // Baru, Diproses, Selesai
    val priority: String? = null // Mendesak
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPerbaikanScreen(
    onBack: () -> Unit = {},
    onOpenDetail: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterIndex by remember { mutableIntStateOf(0) }

    val allReports = remember {
        listOf(
            AdminRepairItem("1", "#PR-0012", "Keran kamar mandi bocor", "Siti Aminah", 101, "Air terus menetes meski keran sudah ditutup.", "11 Nov 2026 • 08.30", "Baru", "Mendesak"),
            AdminRepairItem("2", "#PR-0011", "Lampu kamar mati", "Andi", 102, "Lampu utama tidak menyala sejak tadi malam.", "11 Nov 2026 • 07.15", "Baru"),
            AdminRepairItem("3", "#PR-0010", "AC kurang dingin", "Rizky Pratama", 104, "AC menyala, tetapi kamar tetap terasa panas.", "10 Nov 2026 • 16.20", "Diproses"),
            AdminRepairItem("4", "#PR-0009", "Kunci pintu sulit diputar", "Dewi Lestari", 106, "Silinder kunci sudah diganti oleh teknisi.", "9 Nov 2026 • 10.00", "Selesai")
        )
    }

    val filtered = allReports.filter {
        val matchQuery = it.title.contains(searchQuery, ignoreCase = true) ||
                it.tenantName.contains(searchQuery, ignoreCase = true) ||
                it.roomNumber.toString().contains(searchQuery)
        val matchFilter = when (selectedFilterIndex) {
            1 -> it.statusLabel == "Baru"
            2 -> it.statusLabel == "Diproses"
            3 -> it.statusLabel == "Selesai"
            else -> true
        }
        matchQuery && matchFilter
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perbaikan", style = MaterialTheme.typography.titleMedium, color = Ink900) },
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
            Text("Laporan penghuni", style = MaterialTheme.typography.headlineMedium, color = Ink900, modifier = Modifier.entrance(0))
            Text("Pantau keluhan dan perbaikan New Afra Kost.", style = MaterialTheme.typography.bodySmall, color = Ink600, modifier = Modifier.entrance(1))

            Spacer(Modifier.height(16.dp))

            // Stat Summary Cards Row
            Row(Modifier.fillMaxWidth().entrance(2), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatPill("2", "Baru", Modifier.weight(1f))
                StatPill("1", "Diproses", Modifier.weight(1f))
                StatPill("1", "Selesai", Modifier.weight(1f))
            }

            Spacer(Modifier.height(14.dp))

            // Search Bar Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Cari keluhan atau nomor kamar", color = Ink400, style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Ink400) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Green700,
                    unfocusedBorderColor = Line,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                ),
                modifier = Modifier.fillMaxWidth().entrance(3)
            )

            Spacer(Modifier.height(12.dp))

            // Filter Chips Row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.entrance(4)) {
                FilterPillChip("Semua (4)", selected = selectedFilterIndex == 0, onClick = { selectedFilterIndex = 0 })
                FilterPillChip("Baru (2)", selected = selectedFilterIndex == 1, onClick = { selectedFilterIndex = 1 })
                FilterPillChip("Diproses (1)", selected = selectedFilterIndex == 2, onClick = { selectedFilterIndex = 2 })
                FilterPillChip("Selesai (1)", selected = selectedFilterIndex == 3, onClick = { selectedFilterIndex = 3 })
            }

            Spacer(Modifier.height(14.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("${filtered.size} laporan", style = MaterialTheme.typography.titleSmall, color = Ink900)
                Text("Terbaru ∨", style = MaterialTheme.typography.bodySmall, color = Ink600)
            }

            Spacer(Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered, key = { it.id }) { item ->
                    Surface(
                        onClick = { onOpenDetail(item.id) },
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
                                Text(item.code, style = MaterialTheme.typography.labelSmall, color = Ink400, fontSize = 10.sp)

                                val (bgCol, textCol) = when (item.statusLabel) {
                                    "Baru" -> Pair(DangerBg, Danger)
                                    "Diproses" -> Pair(WarningBg, Warning)
                                    else -> Pair(Green50, Green700)
                                }
                                Surface(shape = RoundedCornerShape(6.dp), color = bgCol) {
                                    Text(item.statusLabel, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = textCol, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp)
                                }
                            }

                            Spacer(Modifier.height(4.dp))
                            Text(item.title, style = MaterialTheme.typography.titleSmall, color = Ink900)
                            Text("${item.tenantName} • Kamar ${item.roomNumber}", style = MaterialTheme.typography.labelSmall, color = Green700, fontWeight = FontWeight.SemiBold)

                            Spacer(Modifier.height(4.dp))
                            Text(item.body, style = MaterialTheme.typography.bodySmall, color = Ink600, maxLines = 2)

                            Spacer(Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(item.dateStr, style = MaterialTheme.typography.labelSmall, color = Ink400, fontSize = 10.sp)

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (item.priority != null) {
                                        Surface(shape = RoundedCornerShape(4.dp), color = DangerBg) {
                                            Text(item.priority, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Danger, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 9.sp)
                                        }
                                        Spacer(Modifier.width(6.dp))
                                    }
                                    Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = Ink400, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatPill(value: String, label: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Green50,
        border = BorderStroke(1.dp, Line),
        modifier = modifier.height(56.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(4.dp)
        ) {
            Text(value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Green700)
            Text(label, style = MaterialTheme.typography.labelSmall, color = Ink600, fontSize = 10.sp)
        }
    }
}

@Composable
private fun FilterPillChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = if (selected) Green700 else White,
        border = BorderStroke(1.dp, if (selected) Green700 else Line),
        modifier = Modifier.height(32.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 10.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = if (selected) White else Ink900, fontSize = 11.sp)
        }
    }
}
