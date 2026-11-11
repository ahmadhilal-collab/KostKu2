package com.kostku.app.ui.screens.announcement

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Announcement
import androidx.compose.material.icons.outlined.Warning
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
import com.kostku.app.data.local.AnnouncementEntity
import com.kostku.app.ui.components.entrance
import com.kostku.app.ui.theme.Danger
import com.kostku.app.ui.theme.DangerBg
import com.kostku.app.ui.theme.Gold100
import com.kostku.app.ui.theme.Green50
import com.kostku.app.ui.theme.Green700
import com.kostku.app.ui.theme.Ink400
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900
import com.kostku.app.ui.theme.Line
import com.kostku.app.ui.theme.Warning
import com.kostku.app.ui.theme.WarningBg
import com.kostku.app.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengumumanListScreen(
    announcements: List<AnnouncementEntity>,
    onBack: () -> Unit,
    onViewBill: () -> Unit
) {
    var filterIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengumuman & Notifikasi", style = MaterialTheme.typography.titleMedium, color = Ink900) },
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
            Text("Kabar untuk Anda", style = MaterialTheme.typography.headlineMedium, color = Ink900, modifier = Modifier.entrance(0))
            Text("Andi • Kamar 102 • New Afra Kost", style = MaterialTheme.typography.bodySmall, color = Ink600, modifier = Modifier.entrance(1))

            Spacer(Modifier.height(14.dp))

            // Filter Pills
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.entrance(2)) {
                PillChip("Semua", selected = filterIndex == 0, onClick = { filterIndex = 0 }, modifier = Modifier.weight(1f))
                PillChip("Pengumuman", selected = filterIndex == 1, onClick = { filterIndex = 1 }, modifier = Modifier.weight(1f))
                PillChip("Pembayaran", selected = filterIndex == 2, onClick = { filterIndex = 2 }, modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            // Due Date Warning Alert Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = WarningBg,
                border = BorderStroke(1.dp, Gold100),
                modifier = Modifier
                    .fillMaxWidth()
                    .entrance(3)
            ) {
                Column(Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Warning, contentDescription = null, tint = Warning, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Jatuh tempo 7 hari lagi", style = MaterialTheme.typography.titleSmall, color = Warning)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text("Tagihan November 2026 sebesar Rp800.000 belum dibayar. Jatuh tempo 10 November 2026.", style = MaterialTheme.typography.bodySmall, color = Ink900)

                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(6.dp), color = DangerBg) {
                            Text("BELUM DIBAYAR", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Danger, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp)
                        }
                        Text(
                            text = "Lihat tagihan ›",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Warning,
                            modifier = Modifier.clickable { onViewBill() }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(announcements, key = { it.id }) { item ->
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
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Icon(Icons.Outlined.Announcement, contentDescription = null, tint = Green700, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(item.title, style = MaterialTheme.typography.titleSmall, color = Ink900)
                                }
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(Green700, CircleShape)
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("${item.author} • ${item.publishedAt}", style = MaterialTheme.typography.labelSmall, color = Ink400, fontSize = 10.sp)
                            Spacer(Modifier.height(6.dp))
                            Text(item.body, style = MaterialTheme.typography.bodySmall, color = Ink600)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PillChip(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) Green700 else White,
        border = BorderStroke(1.dp, if (selected) Green700 else Line),
        modifier = modifier.height(36.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = if (selected) White else Ink900)
        }
    }
}
