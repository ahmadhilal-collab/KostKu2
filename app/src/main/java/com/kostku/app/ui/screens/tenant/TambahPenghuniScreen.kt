package com.kostku.app.ui.screens.tenant

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kostku.app.ui.components.KostKuTextField
import com.kostku.app.ui.components.PrimaryButton
import com.kostku.app.ui.components.entrance
import com.kostku.app.ui.theme.Green50
import com.kostku.app.ui.theme.Green700
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900
import com.kostku.app.ui.theme.Line
import com.kostku.app.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahPenghuniScreen(
    onBack: () -> Unit,
    onSubmit: suspend (String, String, String, Int, Long) -> Boolean
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var nik by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var roomNoStr by remember { mutableStateOf("101") }
    var entryDate by remember { mutableStateOf("3 November 2026") }
    var rateStr by remember { mutableStateOf("Rp800.000") }

    var loading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Penghuni", style = MaterialTheme.typography.titleMedium, color = Ink900) },
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
            Text("Data penghuni baru", style = MaterialTheme.typography.headlineMedium, color = Ink900, modifier = Modifier.entrance(0))
            Text("Lengkapi data sesuai identitas penghuni.", style = MaterialTheme.typography.bodySmall, color = Ink600, modifier = Modifier.entrance(1))

            Spacer(Modifier.height(20.dp))

            KostKuTextField(
                label = "Nama lengkap",
                value = name,
                onValueChange = { name = it },
                placeholder = "Masukkan nama lengkap",
                modifier = Modifier.entrance(2)
            )

            Spacer(Modifier.height(14.dp))

            KostKuTextField(
                label = "NIK",
                value = nik,
                onValueChange = { nik = it },
                placeholder = "Masukkan 16 digit NIK",
                keyboardType = KeyboardType.Number,
                modifier = Modifier.entrance(3)
            )

            Spacer(Modifier.height(14.dp))

            KostKuTextField(
                label = "Nomor HP",
                value = phone,
                onValueChange = { phone = it },
                placeholder = "08xx xxxx xxxx",
                keyboardType = KeyboardType.Phone,
                modifier = Modifier.entrance(4)
            )

            Spacer(Modifier.height(14.dp))

            KostKuTextField(
                label = "Nomor Kamar",
                value = roomNoStr,
                onValueChange = { roomNoStr = it },
                placeholder = "Contoh: 101",
                keyboardType = KeyboardType.Number,
                modifier = Modifier.entrance(5)
            )

            Spacer(Modifier.height(14.dp))

            KostKuTextField(
                label = "Tanggal masuk",
                value = entryDate,
                onValueChange = { entryDate = it },
                placeholder = "3 November 2026",
                modifier = Modifier.entrance(6)
            )

            Spacer(Modifier.height(14.dp))

            KostKuTextField(
                label = "Tarif per bulan (All-In)",
                value = rateStr,
                onValueChange = { rateStr = it },
                placeholder = "Rp800.000",
                imeAction = ImeAction.Done,
                modifier = Modifier.entrance(7)
            )

            Spacer(Modifier.height(16.dp))

            Surface(shape = RoundedCornerShape(10.dp), color = Green50, border = BorderStroke(1.dp, Line), modifier = Modifier.entrance(8)) {
                Text(
                    text = "ⓘ NIK dan nomor HP digunakan untuk verifikasi akses penghuni. Pastikan data sudah benar.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Green700,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            PrimaryButton(
                text = "Simpan Data Penghuni",
                loading = loading,
                onClick = {
                    if (name.isEmpty()) {
                        Toast.makeText(context, "Mohon isi nama lengkap penghuni", Toast.LENGTH_SHORT).show()
                        return@PrimaryButton
                    }
                    loading = true
                    coroutineScope.launch {
                        val roomNo = roomNoStr.toIntOrNull() ?: 101
                        val success = onSubmit(name, nik, phone, roomNo, 800_000L)
                        loading = false
                        if (success) {
                            Toast.makeText(context, "Data penghuni $name berhasil disimpan!", Toast.LENGTH_SHORT).show()
                            onBack()
                        } else {
                            Toast.makeText(context, "Gagal menyimpan data penghuni", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.entrance(9)
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}
