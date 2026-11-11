package com.kostku.app.ui.screens.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.HomeWork
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kostku.app.ui.components.BrandHeader
import com.kostku.app.ui.components.PrimaryButton
import com.kostku.app.ui.components.ScreenContainer
import com.kostku.app.ui.components.entrance
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
import kotlinx.coroutines.launch

data class OnboardingPageData(
    val badge: String,
    val title: String,
    val description: String,
    val features: List<OnboardingFeatureItem>
)

data class OnboardingFeatureItem(
    val icon: ImageVector,
    val title: String,
    val desc: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onStart: () -> Unit) {
    val pages = listOf(
        OnboardingPageData(
            badge = "Selamat Datang di KostKu",
            title = "Pengelolaan New Afra Kost Jadi Lebih Praktis",
            description = "Sistem manajemen hunian kost modern yang dirancang khusus untuk mempermudah Pengelola (Pak Abbas) & Penghuni dalam satu tempat.",
            features = listOf(
                OnboardingFeatureItem(
                    icon = Icons.Outlined.HomeWork,
                    title = "Denah & Status Real-Time",
                    desc = "Pantau keterisian 36 kamar Lantai 1 & 2 secara visual dengan warna status interaktif."
                ),
                OnboardingFeatureItem(
                    icon = Icons.Outlined.VerifiedUser,
                    title = "Akses Terverifikasi",
                    desc = "Pemisahan hak akses aman antara Pengelola dan Penghuni."
                )
            )
        ),
        OnboardingPageData(
            badge = "Kemudahan Transaksi",
            title = "Pencatatan Pembayaran & Nota Digital Otomatis",
            description = "Kelola konfirmasi uang tunai dengan cepat, pengingat tagihan WhatsApp 1-klik, dan laporan kerusakan fasilitas transparan.",
            features = listOf(
                OnboardingFeatureItem(
                    icon = Icons.Outlined.Payments,
                    title = "Catat Pembayaran Tunai",
                    desc = "Konfirmasi uang masuk, penerbitan Nota Lunas digital otomatis."
                ),
                OnboardingFeatureItem(
                    icon = Icons.Outlined.NotificationsActive,
                    title = "Pengingat via WhatsApp",
                    desc = "Kirim pesan pengingat jatuh tempo ke penghuni dalam 1 ketukan."
                ),
                OnboardingFeatureItem(
                    icon = Icons.Outlined.Build,
                    title = "Lapor Kerusakan Fasilitas",
                    desc = "Penghuni dapat melapor kerusakan fasilitas lengkap dengan bukti foto."
                )
            )
        ),
        OnboardingPageData(
            badge = "Siap Memulai",
            title = "Panduan Memulai Penggunaan Aplikasi",
            description = "Pilih peran Anda untuk masuk ke dalam aplikasi: sebagai Pengelola Kost (Pak Abbas) atau sebagai Penghuni Kost.",
            features = listOf(
                OnboardingFeatureItem(
                    icon = Icons.Outlined.HomeWork,
                    title = "Peran Pengelola (Pak Abbas)",
                    desc = "Akses penuh denah kamar, data penghuni, penerbitan pengumuman, dan konfirmasi pembayaran."
                ),
                OnboardingFeatureItem(
                    icon = Icons.Outlined.VerifiedUser,
                    title = "Peran Penghuni Kost",
                    desc = "Lihat rincian tagihan bulanan, histori nota lunas, dan ajukan perbaikan fasilitas."
                )
            )
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    ScreenContainer {
        Spacer(Modifier.height(16.dp))

        // Top Navigation Header: Logo + Skip Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .entrance(0),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BrandHeader()
            AnimatedVisibility(visible = pagerState.currentPage < pages.size - 1) {
                Text(
                    text = "Lewati",
                    style = MaterialTheme.typography.labelMedium,
                    color = Ink600,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onStart() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Horizontal Pager for Onboarding Pages
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { pageIndex ->
            val page = pages[pageIndex]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
            ) {
                // Page Badge Chip
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Green50,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Line),
                    modifier = Modifier.entrance(1)
                ) {
                    Text(
                        text = page.badge,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Green700,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Title & Description
                Text(
                    text = page.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Ink900,
                    modifier = Modifier.entrance(2)
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = page.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Ink600,
                    modifier = Modifier.entrance(3)
                )

                Spacer(Modifier.height(24.dp))

                // Feature Items List
                page.features.forEachIndexed { fIndex, feature ->
                    OnboardingFeatureCard(
                        feature = feature,
                        modifier = Modifier.entrance(4 + fIndex)
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }

        // Bottom Navigation Bar: Indicators + Action Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Page Indicator Dots
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pages.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (isSelected) 24.dp else 8.dp, 8.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) Green700 else Ink400.copy(alpha = 0.4f))
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Primary Action Button (Lanjut vs Mulai Sekarang)
            val isLastPage = pagerState.currentPage == pages.size - 1
            PrimaryButton(
                text = if (isLastPage) "Mulai Sekarang" else "Lanjutkan Tour",
                onClick = {
                    if (isLastPage) {
                        onStart()
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier.entrance(7)
            )
        }
    }
}

@Composable
private fun OnboardingFeatureCard(
    feature: OnboardingFeatureItem,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Line),
        shadowElevation = 1.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Green50, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    tint = Green700,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = feature.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Ink900
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = feature.desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = Ink600,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
