package com.kostku.app.ui.screens.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kostku.app.data.local.PaymentEntity
import com.kostku.app.ui.components.entrance
import com.kostku.app.ui.theme.Danger
import com.kostku.app.ui.theme.DangerBg
import com.kostku.app.ui.theme.Green50
import com.kostku.app.ui.theme.Green700
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900
import com.kostku.app.ui.theme.Line
import com.kostku.app.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoriPembayaranScreen(
    onBack: () -> Unit,
    onOpenReceipt: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pembayaran", style = MaterialTheme.typography.titleMedium, color = Ink900) },
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
            Text("Status & Histori Pembayaran", style = MaterialTheme.typography.headlineMedium, color = Ink900, modifier = Modifier.entrance(0))
            Text("Andi • Kamar 102 • New Afra Kost", style = MaterialTheme.typography.bodySmall, color = Ink600, modifier = Modifier.entrance(1))

            Spacer(Modifier.height(16.dp))

            // Current Month Active Bill Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = White,
                border = BorderStroke(1.dp, Line),
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .entrance(2)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("November 2026", style = MaterialTheme.typography.titleMedium, color = Ink900)
                        Surface(shape = RoundedCornerShape(6.dp), color = DangerBg) {
                            Text("BELUM DIBAYAR", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Danger, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text("Total Tagihan All-In", style = MaterialTheme.typography.bodySmall, color = Ink600)
                    Text("Rp800.000", style = MaterialTheme.typography.headlineMedium, color = Ink900)

                    Spacer(Modifier.height(10.dp))
                    HorizontalDivider(color = Line)
                    Spacer(Modifier.height(10.dp))

                    Text("📅 Jatuh tempo: 10 November 2026", style = MaterialTheme.typography.bodySmall, color = Ink600)
                    Text("Nota November tersedia setelah pembayaran dikonfirmasi lunas oleh pengelola.", style = MaterialTheme.typography.labelSmall, color = com.kostku.app.ui.theme.Ink400)
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Histori pembayaran", style = MaterialTheme.typography.titleMedium, color = Ink900)
                Text("2026 ∨", style = MaterialTheme.typography.bodySmall, color = Ink600)
            }

            Spacer(Modifier.height(12.dp))

            val pastHistory = remember {
                listOf(
                    Pair("Oktober 2026", "Tunai • 5 Oktober 2026"),
                    Pair("September 2026", "Tunai • 5 September 2026"),
                    Pair("Agustus 2026", "Tunai • 4 Agustus 2026")
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(pastHistory) { (period, dateStr) ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = White,
                        border = BorderStroke(1.dp, Line),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(period, style = MaterialTheme.typography.titleSmall, color = Ink900)
                                Surface(shape = RoundedCornerShape(6.dp), color = Green50) {
                                    Text("LUNAS", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Green700, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("Rp800.000", style = MaterialTheme.typography.titleMedium, color = Ink900)
                            Spacer(Modifier.height(6.dp))

                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(dateStr, style = MaterialTheme.typography.bodySmall, color = com.kostku.app.ui.theme.Ink400)
                                Surface(
                                    onClick = { onOpenReceipt(102) },
                                    color = androidx.compose.ui.graphics.Color.Transparent
                                ) {
                                    Text("Lihat nota ›", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Green700)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
