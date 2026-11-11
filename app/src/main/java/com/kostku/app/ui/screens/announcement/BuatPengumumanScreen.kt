package com.kostku.app.ui.screens.announcement

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kostku.app.ui.components.KostKuTextField
import com.kostku.app.ui.components.PrimaryButton
import com.kostku.app.ui.components.entrance
import com.kostku.app.ui.theme.Green50
import com.kostku.app.ui.theme.Green700
import com.kostku.app.ui.theme.Ink400
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900
import com.kostku.app.ui.theme.Line
import com.kostku.app.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuatPengumumanScreen(
    onBack: () -> Unit,
    onSubmit: suspend (String, String, String) -> Long
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("Semua penghuni") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buat Pengumuman", style = MaterialTheme.typography.titleMedium, color = Ink900) },
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
            Text("Informasi untuk penghuni", style = MaterialTheme.typography.headlineMedium, color = Ink900, modifier = Modifier.entrance(0))
            Text("Sampaikan kabar terbaru di New Afra Kost.", style = MaterialTheme.typography.bodySmall, color = Ink600, modifier = Modifier.entrance(1))

            Spacer(Modifier.height(20.dp))

            KostKuTextField(
                label = "Judul pengumuman",
                value = title,
                onValueChange = { title = it },
                placeholder = "Pembersihan area bersama",
                modifier = Modifier.entrance(2)
            )

            Spacer(Modifier.height(14.dp))

            Text("Isi pengumuman", style = MaterialTheme.typography.titleSmall, color = Ink900, modifier = Modifier.entrance(3))
            Spacer(Modifier.height(6.dp))

            OutlinedTextField(
                value = body,
                onValueChange = { body = it },
                placeholder = { Text("Minggu, 8 November, pukul 08.00–10.00. Mohon simpan barang pribadi di koridor dan dapur bersama. Terima kasih.", color = Ink400, style = MaterialTheme.typography.bodyMedium) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .entrance(4),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Green700,
                    unfocusedBorderColor = Line,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            Spacer(Modifier.height(14.dp))

            KostKuTextField(
                label = "Tujuan penerima",
                value = target,
                onValueChange = { target = it },
                placeholder = "Semua penghuni",
                modifier = Modifier.entrance(5)
            )

            Spacer(Modifier.height(16.dp))

            Surface(shape = RoundedCornerShape(12.dp), color = Green50, border = BorderStroke(1.dp, Line), modifier = Modifier.entrance(6)) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = Green700, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Pengumuman akan tampil di halaman penghuni setelah diterbitkan oleh Pak Abbas.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Green700
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            PrimaryButton(
                text = "Terbitkan Pengumuman",
                onClick = {
                    if (title.isEmpty()) {
                        Toast.makeText(context, "Mohon isi judul pengumuman", Toast.LENGTH_SHORT).show()
                        return@PrimaryButton
                    }
                    if (body.isEmpty()) {
                        Toast.makeText(context, "Mohon isi pengumuman", Toast.LENGTH_SHORT).show()
                        return@PrimaryButton
                    }
                    coroutineScope.launch {
                        onSubmit(title, body, target)
                        Toast.makeText(context, "Pengumuman berhasil diterbitkan!", Toast.LENGTH_SHORT).show()
                        onBack()
                    }
                },
                modifier = Modifier.entrance(7)
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}
