package com.kostku.app.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kostku.app.ui.components.BackButton
import com.kostku.app.ui.components.BrandHeader
import com.kostku.app.ui.components.ErrorBanner
import com.kostku.app.ui.components.KostKuTextField
import com.kostku.app.ui.components.PrimaryButton
import com.kostku.app.ui.components.ScreenContainer
import com.kostku.app.ui.components.entrance
import com.kostku.app.ui.theme.Green700
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900

@Composable
fun AdminRegisterScreen(
    onBack: () -> Unit,
    onGoLogin: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
) {
    val state by viewModel.ui.collectAsStateWithLifecycle()
    var name by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var secret by rememberSaveable { mutableStateOf("") }
    val focus = LocalFocusManager.current

    LaunchedEffect(state.success) { if (state.success) onSuccess() }

    val submit = {
        focus.clearFocus()
        viewModel.registerAdmin(name, phone, password, secret)
    }

    ScreenContainer(scrollable = true) {
        Spacer(Modifier.height(4.dp))
        BackButton(onBack)
        BrandHeader(Modifier.entrance(0).padding(top = 8.dp))
        Spacer(Modifier.height(24.dp))
        Text(
            "Daftar akun admin",
            style = MaterialTheme.typography.headlineLarge,
            color = Ink900,
            modifier = Modifier.entrance(1)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            "Halaman ini khusus untuk pemilik kost. Jika Anda penghuni, silakan masuk sebagai penghuni.",
            style = MaterialTheme.typography.bodyLarge,
            color = Ink600,
            modifier = Modifier.entrance(2)
        )
        Spacer(Modifier.height(24.dp))
        KostKuTextField(
            label = "Nama Lengkap",
            value = name,
            onValueChange = { name = it; viewModel.clearFieldError(AuthField.NAME) },
            placeholder = "Masukkan nama lengkap Anda",
            error = state.fieldErrors[AuthField.NAME],
            enabled = !state.loading,
            modifier = Modifier.entrance(3)
        )
        Spacer(Modifier.height(16.dp))
        KostKuTextField(
            label = "Nomor WhatsApp",
            value = phone,
            onValueChange = { phone = it.filter { c -> c.isDigit() || c == '+' }.take(15); viewModel.clearFieldError(AuthField.PHONE) },
            placeholder = "08xx xxxx xxxx",
            keyboardType = KeyboardType.Phone,
            error = state.fieldErrors[AuthField.PHONE],
            enabled = !state.loading,
            modifier = Modifier.entrance(4)
        )
        Spacer(Modifier.height(16.dp))
        KostKuTextField(
            label = "Kata Sandi",
            value = password,
            onValueChange = { password = it; viewModel.clearFieldError(AuthField.PASSWORD) },
            placeholder = "Minimal 6 karakter",
            isPassword = true,
            error = state.fieldErrors[AuthField.PASSWORD],
            enabled = !state.loading,
            modifier = Modifier.entrance(5)
        )
        Spacer(Modifier.height(16.dp))
        KostKuTextField(
            label = "Sandi Rahasia",
            value = secret,
            onValueChange = { secret = it; viewModel.clearFieldError(AuthField.SECRET) },
            placeholder = "Masukkan sandi rahasia pemilik",
            isPassword = true,
            imeAction = ImeAction.Done,
            onImeAction = { submit() },
            error = state.fieldErrors[AuthField.SECRET],
            enabled = !state.loading,
            modifier = Modifier.entrance(6)
        )
        Spacer(Modifier.height(20.dp))
        ErrorBanner(state.error)
        if (state.error != null) Spacer(Modifier.height(14.dp))
        PrimaryButton(
            text = "Daftar Sekarang",
            onClick = { submit() },
            loading = state.loading,
            modifier = Modifier.entrance(7)
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Sudah punya akun? ", style = MaterialTheme.typography.bodyMedium, color = Ink600)
            Text(
                "Masuk di sini",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Green700,
                modifier = Modifier
                    .clickable(enabled = !state.loading, onClick = onGoLogin)
                    .padding(vertical = 8.dp)
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}
