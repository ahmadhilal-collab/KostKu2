package com.kostku.app.ui.screens.tenant

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.HomeWork
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kostku.app.ui.components.KostKuLogo
import com.kostku.app.ui.components.entrance
import com.kostku.app.ui.theme.Danger
import com.kostku.app.ui.theme.DangerBg
import com.kostku.app.ui.theme.Gold100
import com.kostku.app.ui.theme.Gold500
import com.kostku.app.ui.theme.Green50
import com.kostku.app.ui.theme.Green700
import com.kostku.app.ui.theme.Green900
import com.kostku.app.ui.theme.Ink400
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900
import com.kostku.app.ui.theme.Line
import com.kostku.app.ui.theme.White

@Composable
fun ProfilInfoContent(
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text("Info Aplikasi & Profil Pencipta", style = MaterialTheme.typography.headlineMedium, color = Ink900, modifier = Modifier.entrance(0))
        Text("Sistem Manajemen Hunian Pintar New Afra Kost", style = MaterialTheme.typography.bodySmall, color = Ink600, modifier = Modifier.entrance(1))

        Spacer(Modifier.height(20.dp))

        // App Identity Hero Card
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = Green50,
            border = BorderStroke(1.dp, Line),
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .entrance(2)
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                KostKuLogo(size = 48.dp)
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("KostKu", style = MaterialTheme.typography.headlineSmall, color = Green900)
                    Spacer(Modifier.height(2.dp))
                    Text("Versi 1.0.0 (Official Stable Release)", style = MaterialTheme.typography.bodySmall, color = Green700, fontWeight = FontWeight.SemiBold)
                }
                Surface(shape = RoundedCornerShape(20.dp), color = Gold100) {
                    Text("PRO", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Gold500, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Deskripsi Aplikasi Card
        Text("Deskripsi Aplikasi", style = MaterialTheme.typography.titleMedium, color = Ink900, modifier = Modifier.entrance(3))
        Spacer(Modifier.height(8.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = White,
            border = BorderStroke(1.dp, Line),
            shadowElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .entrance(4)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "KostKu v1.0 adalah sistem pengelolaan hunian kost pintar berorientasi masa depan yang dirancang khusus untuk mempermudah pengelolaan New Afra Kost. Aplikasi ini mengintegrasikan seluruh operasional kost dalam satu platform terpadu: pencatatan pembayaran tunai otomatis, pemantauan denah hunian real-time, transparansi nota digital, pengingat jatuh tempo via WhatsApp 1-klik, penerbitan pengumuman kost, hingga pelaporan dan tindak lanjut perbaikan fasilitas oleh penghuni secara digital.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Ink900,
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Fitur Utama Highlight Cards
        Text("Fitur Utama KostKu", style = MaterialTheme.typography.titleMedium, color = Ink900, modifier = Modifier.entrance(5))
        Spacer(Modifier.height(10.dp))

        Column(
            modifier = Modifier.entrance(6),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FeatureHighlightRow(Icons.Outlined.HomeWork, "Pemantauan Denah Kamar Visual", "Peta denahhunian warna status real-time untuk 36 kamar.")
            FeatureHighlightRow(Icons.Outlined.Payments, "Pencatatan Bayar Tunai & Nota Digital", "Penerbitan nota resmi lunas otomatis & ekspor file gambar/PDF.")
            FeatureHighlightRow(Icons.Outlined.NotificationsActive, "Pengingat WhatsApp 1-Klik", "Kirim pengingat jatuh tempo instan ke WhatsApp penghuni.")
            FeatureHighlightRow(Icons.Outlined.Build, "Pelaporan Kerusakan Fasilitas", "Penghuni dapat melaporkan perbaikan lengkap dengan bukti foto.")
        }

        Spacer(Modifier.height(20.dp))
        HorizontalDivider(color = Line)
        Spacer(Modifier.height(20.dp))

        // Informasi Mitra Section
        Text("Informasi Mitra Kost", style = MaterialTheme.typography.titleMedium, color = Ink900, modifier = Modifier.entrance(7))
        Spacer(Modifier.height(10.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = White,
            border = BorderStroke(1.dp, Line),
            modifier = Modifier
                .fillMaxWidth()
                .entrance(8)
        ) {
            Column(Modifier.padding(16.dp)) {
                MitraDetailItem("Nama Mitra", "New Afra Kost")
                MitraDetailItem("Alamat Lokasi", "Jl. Perintis Kemerdekaan 4, Makassar")
                MitraDetailItem("Pemilik Kost", "Hj. Muhammad Sahrul Latif")
                MitraDetailItem("Pengelola Kost", "Pak Abbas")
                MitraDetailItem("Kapasitas Kamar", "36 Kamar All-In (Lantai 1 & 2)")
            }
        }

        Spacer(Modifier.height(20.dp))
        HorizontalDivider(color = Line)
        Spacer(Modifier.height(20.dp))

        // Tim Pengembang Section
        Text("Tim Pengembang", style = MaterialTheme.typography.titleMedium, color = Ink900, modifier = Modifier.entrance(9))
        Spacer(Modifier.height(10.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Green50,
            border = BorderStroke(1.dp, Line),
            modifier = Modifier
                .fillMaxWidth()
                .entrance(10)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Code, contentDescription = null, tint = Green700, modifier = Modifier.size(24.dp))
                }
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Anak Baik Team", style = MaterialTheme.typography.titleSmall, color = Ink900)
                    Spacer(Modifier.height(2.dp))
                    Text("D4 Teknik Komputer dan Jaringan - PNUP", style = MaterialTheme.typography.bodySmall, color = Green700, fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        // Logout Button
        OutlinedButton(
            onClick = onLogout,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.2.dp, Danger),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = DangerBg),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .entrance(11)
        ) {
            Text("Keluar dari Akun (Logout)", style = MaterialTheme.typography.labelLarge, color = Danger)
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun FeatureHighlightRow(icon: ImageVector, title: String, desc: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = White,
        border = BorderStroke(1.dp, Line),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Green50, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Green700, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = Ink900)
                Text(desc, style = MaterialTheme.typography.labelSmall, color = Ink600, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun MitraDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Ink600)
        Text(value, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold), color = Ink900)
    }
}
