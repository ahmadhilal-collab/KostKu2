package com.kostku.app.ui.screens.repair

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kostku.app.ui.components.PrimaryButton
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailLaporanScreen(
    reportId: String = "1",
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var statusState by remember { mutableStateOf("Baru") }
    var expandedStatusMenu by remember { mutableStateOf(false) }
    var noteInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Laporan", style = MaterialTheme.typography.titleMedium, color = Ink900) },
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().entrance(0),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("#PR-0012", style = MaterialTheme.typography.labelSmall, color = Ink400)
                Surface(shape = RoundedCornerShape(6.dp), color = DangerBg) {
                    Text("Baru", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Danger, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                }
            }

            Spacer(Modifier.height(6.dp))
            Text("Keran kamar mandi bocor", style = MaterialTheme.typography.headlineSmall, color = Ink900, modifier = Modifier.entrance(1))
            Text("Dilaporkan 11 November 2026 • 08.30", style = MaterialTheme.typography.bodySmall, color = Ink600, modifier = Modifier.entrance(2))

            Spacer(Modifier.height(16.dp))

            // Priority Danger Warning Card
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DangerBg,
                border = BorderStroke(1.dp, Danger.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth().entrance(3)
            ) {
                Column(Modifier.padding(14.dp)) {
                    Text("Mendesak • Kebocoran air", style = MaterialTheme.typography.titleSmall, color = Danger)
                    Spacer(Modifier.height(2.dp))
                    Text("Segera tinjau agar air tidak terbuang dan lantai tidak licin.", style = MaterialTheme.typography.bodySmall, color = Danger)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Informasi Penghuni Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = White,
                border = BorderStroke(1.dp, Line),
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth().entrance(4)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Informasi penghuni", style = MaterialTheme.typography.titleSmall, color = Ink900)
                    Spacer(Modifier.height(8.dp))
                    Text("Siti Aminah", style = MaterialTheme.typography.titleMedium, color = Ink900)
                    Text("Kamar 101 • Lantai 1", style = MaterialTheme.typography.bodySmall, color = Ink600)

                    Spacer(Modifier.height(12.dp))

                    Surface(
                        onClick = {
                            val phone = "+6281234567801"
                            val message = "Halo Siti Aminah, mengenai laporan kerusakan keran kamar mandi Kamar 101, sedang kami tindak lanjuti. Terima kasih - Pak Abbas."
                            val url = "https://api.whatsapp.com/send?phone=$phone&text=${Uri.encode(message)}"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Aplikasi WhatsApp tidak ditemukan", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = Green50,
                        border = BorderStroke(1.dp, Line),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            Icon(Icons.Outlined.Chat, contentDescription = null, tint = Green700, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Hubungi penghuni via WhatsApp", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = Green700)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Detail Keluhan Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = White,
                border = BorderStroke(1.dp, Line),
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth().entrance(5)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Detail keluhan", style = MaterialTheme.typography.titleSmall, color = Ink900)
                    Spacer(Modifier.height(6.dp))
                    Text("Kategori: Air & sanitasi", style = MaterialTheme.typography.labelSmall, color = Green700, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("Keran di kamar mandi terus menetes meskipun sudah ditutup rapat. Lantai jadi sering basah. Mohon dibantu diperiksa dan diperbaiki, Pak.", style = MaterialTheme.typography.bodyMedium, color = Ink900, lineHeight = 20.sp)
                    Spacer(Modifier.height(10.dp))
                    Text("Lampiran: tidak ada", style = MaterialTheme.typography.labelSmall, color = Ink400)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Tindak Lanjut Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = White,
                border = BorderStroke(1.dp, Line),
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth().entrance(6)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Tindak lanjut", style = MaterialTheme.typography.titleSmall, color = Ink900)
                    Spacer(Modifier.height(8.dp))
                    Text("Status laporan", style = MaterialTheme.typography.labelSmall, color = Ink600)
                    Spacer(Modifier.height(4.dp))

                    Box {
                        Surface(
                            onClick = { expandedStatusMenu = true },
                            shape = RoundedCornerShape(10.dp),
                            color = White,
                            border = BorderStroke(1.dp, Line),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(statusState, style = MaterialTheme.typography.bodyMedium, color = Ink900)
                                Text("∨", style = MaterialTheme.typography.titleSmall, color = Ink600)
                            }
                        }

                        DropdownMenu(
                            expanded = expandedStatusMenu,
                            onDismissRequest = { expandedStatusMenu = false }
                        ) {
                            DropdownMenuItem(text = { Text("Baru") }, onClick = { statusState = "Baru"; expandedStatusMenu = false })
                            DropdownMenuItem(text = { Text("Diproses") }, onClick = { statusState = "Diproses"; expandedStatusMenu = false })
                            DropdownMenuItem(text = { Text("Selesai") }, onClick = { statusState = "Selesai"; expandedStatusMenu = false })
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    Text("Catatan untuk penghuni", style = MaterialTheme.typography.labelSmall, color = Ink600)
                    Spacer(Modifier.height(4.dp))

                    OutlinedTextField(
                        value = noteInput,
                        onValueChange = { noteInput = it },
                        placeholder = { Text("Tulis rencana penanganan atau jadwal kunjungan teknisi...", color = Ink400, style = MaterialTheme.typography.bodyMedium) },
                        modifier = Modifier.fillMaxWidth().height(90.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Green700,
                            unfocusedBorderColor = Line,
                            focusedContainerColor = White,
                            unfocusedContainerColor = White
                        )
                    )

                    Spacer(Modifier.height(4.dp))
                    Text("Catatan akan terlihat oleh penghuni.", style = MaterialTheme.typography.labelSmall, color = Ink400, fontSize = 10.sp)

                    Spacer(Modifier.height(16.dp))
                    PrimaryButton(
                        text = "Mulai proses perbaikan",
                        onClick = {
                            Toast.makeText(context, "Status laporan perbaikan berhasil diperbarui ke: $statusState!", Toast.LENGTH_SHORT).show()
                            onBack()
                        }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Riwayat Laporan Log Section
            Text("Riwayat laporan", style = MaterialTheme.typography.titleMedium, color = Ink900, modifier = Modifier.entrance(7))
            Spacer(Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Green50,
                border = BorderStroke(1.dp, Line),
                modifier = Modifier.fillMaxWidth().entrance(8)
            ) {
                Column(Modifier.padding(14.dp)) {
                    Text("Laporan diterima", style = MaterialTheme.typography.titleSmall, color = Green700)
                    Text("11 Nov 2026 • 08.30 — Siti Aminah", style = MaterialTheme.typography.labelSmall, color = Ink600)
                    Spacer(Modifier.height(2.dp))
                    Text("Menunggu peninjauan pengelola.", style = MaterialTheme.typography.bodySmall, color = Ink600)
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
