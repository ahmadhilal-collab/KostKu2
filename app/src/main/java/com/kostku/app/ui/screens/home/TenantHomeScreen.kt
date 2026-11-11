package com.kostku.app.ui.screens.home

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
import androidx.compose.material.icons.outlined.Announcement
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kostku.app.data.local.Session
import com.kostku.app.ui.components.KostKuLogo
import com.kostku.app.ui.components.SecondaryButton
import com.kostku.app.ui.components.entrance
import com.kostku.app.ui.screens.repair.LaporKerusakanContent
import com.kostku.app.ui.screens.tenant.ProfilInfoContent
import com.kostku.app.ui.theme.Danger
import com.kostku.app.ui.theme.DangerBg
import com.kostku.app.ui.theme.Green50
import com.kostku.app.ui.theme.Green700
import com.kostku.app.ui.theme.Green900
import com.kostku.app.ui.theme.Ink400
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900
import com.kostku.app.ui.theme.Line
import com.kostku.app.ui.theme.White

@Composable
fun TenantHomeScreen(
    session: Session,
    onLogout: () -> Unit,
    onNavigateToLaporKerusakan: () -> Unit,
    onNavigateToPengumuman: () -> Unit,
    onNavigateToRiwayat: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            TenantBottomNavigation(
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
                0 -> TenantDashboardTab(
                    session = session,
                    onNavigateToLaporKerusakan = onNavigateToLaporKerusakan,
                    onNavigateToPengumuman = onNavigateToPengumuman,
                    onNavigateToRiwayat = onNavigateToRiwayat
                )
                1 -> LaporKerusakanContent(
                    session = session,
                    onSubmitted = { selectedTab = 0 }
                )
                2 -> ProfilInfoContent(
                    onLogout = onLogout
                )
            }
        }
    }
}

@Composable
private fun TenantDashboardTab(
    session: Session,
    onNavigateToLaporKerusakan: () -> Unit,
    onNavigateToPengumuman: () -> Unit,
    onNavigateToRiwayat: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Logo Header
        Row(
            modifier = Modifier.entrance(0),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KostKuLogo(size = 32.dp)
            Spacer(Modifier.width(8.dp))
            Text("KostKu", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold), color = Green900)
        }

        Spacer(Modifier.height(20.dp))

        // Welcome Text
        Text(
            text = "Selamat Datang, ${session.name}" + (session.label?.let { " ($it)" } ?: " (Kamar 102)"),
            style = MaterialTheme.typography.headlineMedium,
            color = Ink900,
            modifier = Modifier.entrance(1)
        )
        Text("New Afra Kost", style = MaterialTheme.typography.bodySmall, color = Ink600, modifier = Modifier.entrance(2))

        Spacer(Modifier.height(20.dp))

        // Active Bill Card (November 2026)
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = White,
            border = BorderStroke(1.dp, Line),
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .entrance(3)
        ) {
            Column(Modifier.padding(18.dp)) {
                Text("Tagihan November 2026", style = MaterialTheme.typography.titleSmall, color = Ink900)
                Spacer(Modifier.height(10.dp))
                Text("Total Tagihan All-In", style = MaterialTheme.typography.bodySmall, color = Ink600)
                Text("Rp 800.000", style = MaterialTheme.typography.headlineLarge, color = Ink900)
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = Line)
                Spacer(Modifier.height(12.dp))
                Text("Jatuh Tempo: 10 November 2026", style = MaterialTheme.typography.bodyMedium, color = Ink600)
                Spacer(Modifier.height(10.dp))

                Surface(shape = RoundedCornerShape(6.dp), color = DangerBg) {
                    Text(
                        text = "Status: BELUM DIBAYAR",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Danger,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Button: Ajukan Perbaikan Fasilitas
        OutlinedButton(
            onClick = onNavigateToLaporKerusakan,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.2.dp, com.kostku.app.ui.theme.Green700),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .entrance(4)
        ) {
            Text("Ajukan Perbaikan Fasilitas", style = MaterialTheme.typography.labelLarge, color = com.kostku.app.ui.theme.Green700)
        }

        Spacer(Modifier.height(20.dp))

        // Menu Cards
        TenantMenuCard(
            title = "Pengumuman & Notifikasi",
            subtitle = "Tenggat pembayaran dan informasi kost terbaru.",
            icon = Icons.Outlined.Announcement,
            onClick = onNavigateToPengumuman,
            modifier = Modifier.entrance(5)
        )

        Spacer(Modifier.height(12.dp))

        TenantMenuCard(
            title = "Status & Riwayat Pembayaran",
            subtitle = "Lihat status tagihan dan history pembayaran.",
            icon = Icons.Outlined.ReceiptLong,
            onClick = onNavigateToRiwayat,
            modifier = Modifier.entrance(6)
        )
    }
}

@Composable
private fun TenantMenuCard(
    title: String,
    subtitle: String,
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
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Green50, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Green700, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, color = Ink900)
                Spacer(Modifier.height(2.dp))
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Ink600, fontSize = 11.sp)
            }
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = Ink400)
        }
    }
}

@Composable
private fun TenantBottomNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = White,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple("Beranda", Icons.Outlined.Home, 0),
            Triple("Perbaikan", Icons.Outlined.Build, 1),
            Triple("Profil & Info", Icons.Outlined.Info, 2)
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
