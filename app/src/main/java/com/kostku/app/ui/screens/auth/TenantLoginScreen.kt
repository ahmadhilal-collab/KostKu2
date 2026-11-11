package com.kostku.app.ui.screens.auth

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.kostku.app.util.PhoneUtils

@Composable
fun TenantLoginScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
) {
    val state by viewModel.ui.collectAsStateWithLifecycle()
    var name by rememberSaveable { mutableStateOf("") }
    var nik by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    val focus = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.loadOwnerPhone() }
    LaunchedEffect(state.success) { if (state.success) onSuccess() }

    val submit = {
        focus.clearFocus()
        viewModel.loginTenant(name, nik, phone)
    }

    ScreenContainer(scrollable = true) {
        Spacer(Modifier.height(4.dp))
        BackButton(onBack)
        BrandHeader(Modifier.entrance(0).padding(top = 8.dp))
        Spacer(Modifier.height(28.dp))
        Text(
            "Masuk sebagai penghuni",
            style = MaterialTheme.typography.headlineLarge,
            color = Ink900,
            modifier = Modifier.entrance(1)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            "Akses tagihan dan informasi New Afra Kost dengan data terdaftar Anda.",
            style = MaterialTheme.typography.bodyLarge,
            color = Ink600,
            modifier = Modifier.entrance(2)
        )
        Spacer(Modifier.height(26.dp))
        KostKuTextField(
            label = "Nama lengkap",
            value = name,
            onValueChange = { name = it; viewModel.clearFieldError(AuthField.NAME) },
            placeholder = "Masukkan nama sesuai identitas",
            error = state.fieldErrors[AuthField.NAME],
            enabled = !state.loading,
            modifier = Modifier.entrance(3)
        )
        Spacer(Modifier.height(16.dp))
        KostKuTextField(
            label = "NIK",
            value = nik,
            onValueChange = { nik = it.filter { c -> c.isDigit() }.take(16); viewModel.clearFieldError(AuthField.NIK) },
            placeholder = "Masukkan 16 digit NIK",
            keyboardType = KeyboardType.Number,
            error = state.fieldErrors[AuthField.NIK],
            enabled = !state.loading,
            modifier = Modifier.entrance(4)
        )
        Spacer(Modifier.height(16.dp))
        KostKuTextField(
            label = "Nomor HP",
            value = phone,
            onValueChange = { phone = it.filter { c -> c.isDigit() || c == '+' }.take(15); viewModel.clearFieldError(AuthField.PHONE) },
            placeholder = "08xx xxxx xxxx",
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Done,
            onImeAction = { submit() },
            error = state.fieldErrors[AuthField.PHONE],
            enabled = !state.loading,
            modifier = Modifier.entrance(5)
        )
        Spacer(Modifier.height(20.dp))
        ErrorBanner(state.error)
        if (state.error != null) Spacer(Modifier.height(14.dp))
        PrimaryButton(
            text = "Masuk sebagai Penghuni",
            onClick = { submit() },
            loading = state.loading,
            modifier = Modifier.entrance(6)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Belum terdaftar? Hubungi Pak Abbas",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = Green700,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { contactOwner(context, state.ownerPhone) }
                .padding(vertical = 10.dp)
        )
        Spacer(Modifier.height(20.dp))
        InfoNote("Gunakan nama, NIK, dan nomor HP yang dicatat pengelola. Data Anda hanya untuk verifikasi akses penghuni.")
        Spacer(Modifier.height(24.dp))
    }
}

private fun contactOwner(context: Context, ownerPhone: String?) {
    if (ownerPhone == null) {
        Toast.makeText(context, "Kontak pengelola belum tersedia.", Toast.LENGTH_SHORT).show()
        return
    }
    val text = Uri.encode("Halo Pak Abbas, saya ingin didaftarkan sebagai penghuni New Afra Kost.")
    val uri = Uri.parse("https://wa.me/${PhoneUtils.toInternational(ownerPhone)}?text=$text")
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "WhatsApp atau browser tidak ditemukan.", Toast.LENGTH_SHORT).show()
    }
}
