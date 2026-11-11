package com.kostku.app.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kostku.app.data.local.Session
import com.kostku.app.data.repository.RoomSummary
import com.kostku.app.ui.components.BrandHeader
import com.kostku.app.ui.components.ScreenContainer
import com.kostku.app.ui.components.SecondaryButton
import com.kostku.app.ui.components.entrance
import com.kostku.app.ui.theme.Gold100
import com.kostku.app.ui.theme.Green50
import com.kostku.app.ui.theme.Green700
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900
import com.kostku.app.ui.theme.Line
import com.kostku.app.ui.theme.Warning

/**
 * Beranda sementara untuk Milestone 1: membuktikan login, sesi, dan database berfungsi.
 * Akan diganti dengan Beranda Admin lengkap di Milestone 2.
 */
@Composable
fun AdminHomePlaceholder(session: Session, summary: RoomSummary, onLogout: () -> Unit) {
    ScreenContainer(scrollable = true) {
        Spacer(Modifier.height(20.dp))
        BrandHeader(Modifier.entrance(0))
        Spacer(Modifier.height(28.dp))
        Text("Halo, ${session.name}", style = MaterialTheme.typography.headlineLarge, color = Ink900, modifier = Modifier.entrance(1))
        Spacer(Modifier.height(6.dp))
        Text("New Afra Kost - Jl. Perintis Kemerdekaan 4", style = MaterialTheme.typography.bodyMedium, color = Ink600, modifier = Modifier.entrance(2))
        Spacer(Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth().entrance(3), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Kamar terisi", summary.occupied.toString(), Modifier.weight(1f))
            StatCard("Kamar kosong", summary.empty.toString(), Modifier.weight(1f))
            StatCard("Perbaikan", summary.maintenance.toString(), Modifier.weight(1f))
        }
        Spacer(Modifier.height(20.dp))
        ComingSoonCard("Fitur admin (denah kamar, data penghuni, pengumuman, pembayaran tunai) hadir di Milestone 2.", Modifier.entrance(4))
        Spacer(Modifier.height(28.dp))
        SecondaryButton("Keluar dari Akun", onLogout, Modifier.entrance(5))
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun TenantHomePlaceholder(session: Session, onLogout: () -> Unit) {
    ScreenContainer(scrollable = true) {
        Spacer(Modifier.height(20.dp))
        BrandHeader(Modifier.entrance(0))
        Spacer(Modifier.height(28.dp))
        Text(
            "Selamat Datang, ${session.name}" + (session.label?.let { " ($it)" } ?: ""),
            style = MaterialTheme.typography.headlineLarge,
            color = Ink900,
            modifier = Modifier.entrance(1)
        )
        Spacer(Modifier.height(6.dp))
        Text("New Afra Kost", style = MaterialTheme.typography.bodyMedium, color = Ink600, modifier = Modifier.entrance(2))
        Spacer(Modifier.height(24.dp))
        ComingSoonCard("Fitur penghuni (tagihan, pengumuman, riwayat pembayaran, laporan fasilitas) hadir di Milestone 3.", Modifier.entrance(3))
        Spacer(Modifier.height(28.dp))
        SecondaryButton("Keluar dari Akun", onLogout, Modifier.entrance(4))
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = Green50,
        border = BorderStroke(1.dp, Line)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(value, style = MaterialTheme.typography.headlineMedium, color = Green700)
            Spacer(Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.bodySmall, color = Ink600)
        }
    }
}

@Composable
private fun ComingSoonCard(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Gold100, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.Construction, contentDescription = null, tint = Warning)
        }
        Spacer(Modifier.width(12.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium, color = Ink900)
    }
}
