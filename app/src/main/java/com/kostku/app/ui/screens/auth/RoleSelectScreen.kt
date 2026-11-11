package com.kostku.app.ui.screens.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.MeetingRoom
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kostku.app.ui.components.BackButton
import com.kostku.app.ui.components.BrandHeader
import com.kostku.app.ui.components.InfoNote
import com.kostku.app.ui.components.RoleCard
import com.kostku.app.ui.components.ScreenContainer
import com.kostku.app.ui.components.entrance
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900

@Composable
fun RoleSelectScreen(
    onAdmin: () -> Unit,
    onTenant: () -> Unit,
    onBack: (() -> Unit)? = null
) {
    ScreenContainer {
        Spacer(Modifier.height(if (onBack != null) 4.dp else 20.dp))
        if (onBack != null) BackButton(onBack)
        BrandHeader(Modifier.entrance(0).padding(top = if (onBack != null) 8.dp else 0.dp))
        Spacer(Modifier.height(36.dp))
        Text(
            "Pilih peran Anda",
            style = MaterialTheme.typography.headlineLarge,
            color = Ink900,
            modifier = Modifier.entrance(1)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            "Masuk sesuai peran Anda untuk mengelola kebutuhan kost.",
            style = MaterialTheme.typography.bodyLarge,
            color = Ink600,
            modifier = Modifier.entrance(2)
        )
        Spacer(Modifier.height(32.dp))
        RoleCard(
            title = "Masuk sebagai Admin",
            description = "Kelola penghuni, pembayaran, dan laporan fasilitas kost.",
            icon = Icons.Outlined.AdminPanelSettings,
            filled = true,
            onClick = onAdmin,
            modifier = Modifier.entrance(3)
        )
        Spacer(Modifier.height(16.dp))
        RoleCard(
            title = "Masuk sebagai Penghuni Kost",
            description = "Lihat tagihan, pengingat pembayaran, dan laporkan kendala fasilitas.",
            icon = Icons.Outlined.MeetingRoom,
            filled = false,
            onClick = onTenant,
            modifier = Modifier.entrance(4)
        )
        Box(Modifier.weight(1f))
        InfoNote(
            "Pilih peran sesuai akun Anda, lalu lanjutkan ke halaman masuk.",
            Modifier.entrance(5)
        )
        Spacer(Modifier.height(24.dp))
    }
}
