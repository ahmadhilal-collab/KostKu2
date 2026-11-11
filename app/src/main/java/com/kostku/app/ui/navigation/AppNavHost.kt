package com.kostku.app.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kostku.app.KostKuApp
import com.kostku.app.data.local.UserRole
import com.kostku.app.ui.screens.announcement.BuatPengumumanScreen
import com.kostku.app.ui.screens.announcement.PengumumanListScreen
import com.kostku.app.ui.screens.auth.AdminLoginScreen
import com.kostku.app.ui.screens.auth.AdminRegisterScreen
import com.kostku.app.ui.screens.auth.OnboardingScreen
import com.kostku.app.ui.screens.auth.RoleSelectScreen
import com.kostku.app.ui.screens.auth.SplashScreen
import com.kostku.app.ui.screens.auth.TenantLoginScreen
import com.kostku.app.ui.screens.home.AdminHomeScreen
import com.kostku.app.ui.screens.home.DenahKamarContent
import com.kostku.app.ui.screens.home.TenantHomeScreen
import com.kostku.app.ui.screens.payment.HistoriPembayaranScreen
import com.kostku.app.ui.screens.payment.NotaPembayaranScreen
import com.kostku.app.ui.screens.payment.PilihNotaScreen
import com.kostku.app.ui.screens.repair.DetailLaporanScreen
import com.kostku.app.ui.screens.room.DetailKamarScreen
import com.kostku.app.ui.screens.room.KatalogKamarScreen
import com.kostku.app.ui.screens.tenant.TambahPenghuniScreen
import com.kostku.app.ui.theme.White
import kotlinx.coroutines.launch

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val ROLE = "role"
    const val ADMIN_LOGIN = "admin_login"
    const val ADMIN_REGISTER = "admin_register"
    const val TENANT_LOGIN = "tenant_login"
    const val ADMIN_HOME = "admin_home"
    const val TENANT_HOME = "tenant_home"
    const val DENAH_KAMAR = "denah_kamar"
    const val DETAIL_KAMAR = "detail_kamar/{roomNo}"
    const val KATALOG_KAMAR = "katalog_kamar"
    const val TAMBAH_PENGHUNI = "tambah_penghuni"
    const val BUAT_PENGUMUMAN = "buat_pengumuman"
    const val NOTA_PEMBAYARAN = "nota_pembayaran/{roomNo}"
    const val PILIH_NOTA = "pilih_nota"
    const val PENGUMUMAN_LIST = "pengumuman_list"
    const val HISTORI_PEMBAYARAN = "histori_pembayaran"
    const val DETAIL_LAPORAN = "detail_laporan/{reportId}"
}

private fun NavHostController.goHomeClearingAll(role: UserRole) {
    val target = if (role == UserRole.ADMIN) Routes.ADMIN_HOME else Routes.TENANT_HOME
    navigate(target) { popUpTo(graph.id) { inclusive = true } }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val app = context.applicationContext as KostKuApp
    val container = app.container
    val coroutineScope = rememberCoroutineScope()

    val appViewModel: AppViewModel = viewModel(factory = AppViewModel.Factory)
    val sessionState by appViewModel.session.collectAsStateWithLifecycle()
    val summary by appViewModel.roomSummary.collectAsStateWithLifecycle()

    val announcements by container.adminRepository.observeAnnouncements().collectAsState(initial = emptyList())
    val rooms by container.adminRepository.observeAllRooms().collectAsState(initial = emptyList())

    // Jika sesi berakhir (logout) saat berada di beranda, kembali ke pilihan peran.
    LaunchedEffect(sessionState) {
        val route = navController.currentDestination?.route
        if (sessionState is SessionState.LoggedOut &&
            (route == Routes.ADMIN_HOME || route == Routes.TENANT_HOME)
        ) {
            navController.navigate(Routes.ROLE) { popUpTo(navController.graph.id) { inclusive = true } }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        enterTransition = { slideInHorizontally(tween(380)) { it / 4 } + fadeIn(tween(380)) },
        exitTransition = { slideOutHorizontally(tween(380)) { -it / 6 } + fadeOut(tween(260)) },
        popEnterTransition = { slideInHorizontally(tween(380)) { -it / 6 } + fadeIn(tween(380)) },
        popExitTransition = { slideOutHorizontally(tween(380)) { it / 4 } + fadeOut(tween(260)) }
    ) {
        composable(Routes.SPLASH, exitTransition = { fadeOut(tween(350)) }) {
            SplashScreen(
                onFinished = {
                    val state = sessionState
                    if (state is SessionState.LoggedIn) {
                        navController.goHomeClearingAll(state.session.role)
                    } else {
                        navController.navigate(Routes.ONBOARDING) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    }
                }
            )
        }
        composable(Routes.ONBOARDING) {
            OnboardingScreen(onStart = { navController.navigate(Routes.ROLE) })
        }
        composable(Routes.ROLE) {
            RoleSelectScreen(
                onAdmin = { navController.navigate(Routes.ADMIN_LOGIN) },
                onTenant = { navController.navigate(Routes.TENANT_LOGIN) },
                onBack = if (navController.previousBackStackEntry != null) {
                    { navController.popBackStack() }
                } else null
            )
        }
        composable(Routes.ADMIN_LOGIN) {
            AdminLoginScreen(
                onBack = { navController.popBackStack() },
                onRegister = { navController.navigate(Routes.ADMIN_REGISTER) },
                onSuccess = { navController.goHomeClearingAll(UserRole.ADMIN) }
            )
        }
        composable(Routes.ADMIN_REGISTER) {
            AdminRegisterScreen(
                onBack = { navController.popBackStack() },
                onGoLogin = { navController.popBackStack() },
                onSuccess = { navController.goHomeClearingAll(UserRole.ADMIN) }
            )
        }
        composable(Routes.TENANT_LOGIN) {
            TenantLoginScreen(
                onBack = { navController.popBackStack() },
                onSuccess = { navController.goHomeClearingAll(UserRole.TENANT) }
            )
        }
        composable(Routes.ADMIN_HOME) {
            val state = sessionState
            if (state is SessionState.LoggedIn) {
                AdminHomeScreen(
                    session = state.session,
                    summary = summary,
                    roomsFlow = container.adminRepository.observeAllRooms(),
                    onLogout = { appViewModel.logout() },
                    onNavigateToDenah = { navController.navigate(Routes.DENAH_KAMAR) },
                    onNavigateToDetail = { roomNo -> navController.navigate("detail_kamar/$roomNo") },
                    onNavigateToKatalog = { navController.navigate(Routes.KATALOG_KAMAR) },
                    onNavigateToDataPenghuni = { navController.navigate(Routes.TAMBAH_PENGHUNI) },
                    onNavigateToBuatPengumuman = { navController.navigate(Routes.BUAT_PENGUMUMAN) },
                    onNavigateToNota = { _ -> navController.navigate(Routes.PILIH_NOTA) },
                    onNavigateToDetailLaporan = { reportId -> navController.navigate("detail_laporan/$reportId") },
                    onRecordPayment = { roomNo -> container.adminRepository.recordCashPayment(roomNo) }
                )
            } else {
                Box(Modifier.fillMaxSize().background(White))
            }
        }
        composable(Routes.TENANT_HOME) {
            val state = sessionState
            if (state is SessionState.LoggedIn) {
                TenantHomeScreen(
                    session = state.session,
                    onLogout = { appViewModel.logout() },
                    onNavigateToLaporKerusakan = { navController.navigate(Routes.PENGUMUMAN_LIST) },
                    onNavigateToPengumuman = { navController.navigate(Routes.PENGUMUMAN_LIST) },
                    onNavigateToRiwayat = { navController.navigate(Routes.HISTORI_PEMBAYARAN) }
                )
            } else {
                Box(Modifier.fillMaxSize().background(White))
            }
        }
        composable(Routes.DENAH_KAMAR) {
            DenahKamarContent(
                rooms = rooms,
                onRoomClick = { room -> navController.navigate("detail_kamar/${room.number}") }
            )
        }
        composable(
            route = Routes.DETAIL_KAMAR,
            arguments = listOf(navArgument("roomNo") { type = NavType.IntType })
        ) { backStackEntry ->
            val roomNo = backStackEntry.arguments?.getInt("roomNo") ?: 101
            DetailKamarScreen(
                roomNumber = roomNo,
                onBack = { navController.popBackStack() },
                onConfirmPayment = { confirmedNo ->
                    navController.navigate("nota_pembayaran/$confirmedNo")
                },
                onAddTenantForRoom = { _ ->
                    navController.navigate(Routes.TAMBAH_PENGHUNI)
                },
                onStatusChanged = { rNo, newStatus ->
                    coroutineScope.launch {
                        container.adminRepository.updateRoomStatus(rNo, newStatus)
                    }
                }
            )
        }
        composable(Routes.KATALOG_KAMAR) {
            KatalogKamarScreen(
                rooms = rooms,
                onBack = { navController.popBackStack() },
                onRoomClick = { roomNo -> navController.navigate("detail_kamar/$roomNo") }
            )
        }
        composable(Routes.TAMBAH_PENGHUNI) {
            TambahPenghuniScreen(
                onBack = { navController.popBackStack() },
                onSubmit = { name, nik, phone, roomNo, rate ->
                    container.adminRepository.addTenant(name, nik, phone, roomNo, rate)
                }
            )
        }
        composable(Routes.BUAT_PENGUMUMAN) {
            BuatPengumumanScreen(
                onBack = { navController.popBackStack() },
                onSubmit = { title, body, target ->
                    container.adminRepository.addAnnouncement(title, body, target)
                }
            )
        }
        composable(Routes.PILIH_NOTA) {
            PilihNotaScreen(
                onBack = { navController.popBackStack() },
                onOpenReceipt = { roomNo -> navController.navigate("nota_pembayaran/$roomNo") }
            )
        }
        composable(
            route = Routes.NOTA_PEMBAYARAN,
            arguments = listOf(navArgument("roomNo") { type = NavType.IntType })
        ) { backStackEntry ->
            val roomNo = backStackEntry.arguments?.getInt("roomNo") ?: 101
            NotaPembayaranScreen(
                roomNumber = roomNo,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.PENGUMUMAN_LIST) {
            PengumumanListScreen(
                announcements = announcements,
                onBack = { navController.popBackStack() },
                onViewBill = { navController.navigate(Routes.HISTORI_PEMBAYARAN) }
            )
        }
        composable(Routes.HISTORI_PEMBAYARAN) {
            HistoriPembayaranScreen(
                onBack = { navController.popBackStack() },
                onOpenReceipt = { roomNo -> navController.navigate("nota_pembayaran/$roomNo") }
            )
        }
        composable(
            route = Routes.DETAIL_LAPORAN,
            arguments = listOf(navArgument("reportId") { type = NavType.StringType })
        ) { backStackEntry ->
            val repId = backStackEntry.arguments?.getString("reportId") ?: "1"
            DetailLaporanScreen(
                reportId = repId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
