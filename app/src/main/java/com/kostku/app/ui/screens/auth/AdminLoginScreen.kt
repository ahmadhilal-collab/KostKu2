package com.kostku.app.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kostku.app.ui.components.BackButton
import com.kostku.app.ui.components.BrandHeader
import com.kostku.app.ui.components.ErrorBanner
import com.kostku.app.ui.components.InfoNote
import com.kostku.app.ui.components.KostKuTextField
import com.kostku.app.ui.components.PrimaryButton
import com.kostku.app.ui.components.ScreenContainer
import com.kostku.app.ui.components.entrance
import com.kostku.app.ui.theme.Green700
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900

@Composable
fun AdminLoginScreen(
    onBack: () -> Unit,
    onRegister: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
) {
    val state by viewModel.ui.collectAsStateWithLifecycle()
    var identifier by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val focus = LocalFocusManager.current

    LaunchedEffect(state.success) { if (state.success) onSuccess() }

    val submit = {
        focus.clearFocus()
        viewModel.loginAdmin(identifier, password)
    }

    ScreenContainer(scrollable = true) {
        Spacer(Modifier.height(4.dp))
        BackButton(onBack)
        BrandHeader(Modifier.entrance(0).padding(top = 8.dp))
        Spacer(Modifier.height(32.dp))
        Text(
            "Selamat datang",
            style = MaterialTheme.typography.headlineLarge,
            color = Ink900,
            modifier = Modifier.entrance(1)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            "Masuk untuk mengelola kebutuhan kost Anda.",
            style = MaterialTheme.typography.bodyLarge,
            color = Ink600,
            modifier = Modifier.entrance(2)
        )
        Spacer(Modifier.height(28.dp))
        KostKuTextField(
            label = "Nama / Nomor HP",
            value = identifier,
            onValueChange = { identifier = it; viewModel.clearFieldError(AuthField.IDENTIFIER) },
            placeholder = "Masukkan nama atau nomor HP",
            error = state.fieldErrors[AuthField.IDENTIFIER],
            enabled = !state.loading,
            modifier = Modifier.entrance(3)
        )
        Spacer(Modifier.height(18.dp))
        KostKuTextField(
            label = "Kata Sandi",
            value = password,
            onValueChange = { password = it; viewModel.clearFieldError(AuthField.PASSWORD) },
            placeholder = "Masukkan kata sandi",
            isPassword = true,
            imeAction = ImeAction.Done,
            onImeAction = { submit() },
            error = state.fieldErrors[AuthField.PASSWORD],
            enabled = !state.loading,
            modifier = Modifier.entrance(4)
        )
        Spacer(Modifier.height(18.dp))
        ErrorBanner(state.error)
        if (state.error != null) Spacer(Modifier.height(14.dp))
        PrimaryButton(
            text = "Masuk ke Akun",
            onClick = { submit() },
            loading = state.loading,
            modifier = Modifier.entrance(5)
        )
        Spacer(Modifier.height(20.dp))
        Text(
            "Daftar Akun Baru",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = Green700,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !state.loading, onClick = onRegister)
                .padding(vertical = 10.dp)
        )
        Spacer(Modifier.height(24.dp))
        InfoNote("Satu akun, akses sesuai peran. Anda otomatis diarahkan ke beranda pengelola atau penghuni.")
        Spacer(Modifier.height(24.dp))
    }
}
