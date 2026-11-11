package com.kostku.app.ui.screens.repair

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kostku.app.data.local.Session
import com.kostku.app.ui.components.PrimaryButton
import com.kostku.app.ui.components.entrance
import com.kostku.app.ui.theme.Green700
import com.kostku.app.ui.theme.Ink400
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900
import com.kostku.app.ui.theme.Line
import com.kostku.app.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun LaporKerusakanContent(
    session: Session,
    onSubmitted: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var category by remember { mutableStateOf("Pilih kategori") }
    var expandedCategory by remember { mutableStateOf(false) }
    val categories = listOf("AC", "Keran/Air", "Listrik", "Bangunan", "Furniture")

    var description by remember { mutableStateOf("") }
    var photoSelected by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text("Lapor Kerusakan Fasilitas", style = MaterialTheme.typography.headlineMedium, color = Ink900, modifier = Modifier.entrance(0))
        Text("${session.name} • ${session.label ?: "Kamar 102"}", style = MaterialTheme.typography.bodySmall, color = Ink600, modifier = Modifier.entrance(1))

        Spacer(Modifier.height(20.dp))

        // Kategori Kerusakan Dropdown
        Text("Kategori Kerusakan", style = MaterialTheme.typography.titleSmall, color = Ink900, modifier = Modifier.entrance(2))
        Spacer(Modifier.height(6.dp))

        Box(modifier = Modifier.fillMaxWidth().entrance(3)) {
            Surface(
                onClick = { expandedCategory = true },
                shape = RoundedCornerShape(12.dp),
                color = White,
                border = BorderStroke(1.dp, Line),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(category, style = MaterialTheme.typography.bodyMedium, color = if (category == "Pilih kategori") Ink400 else Ink900)
                    Text("∨", style = MaterialTheme.typography.titleSmall, color = Ink600)
                }
            }
            DropdownMenu(
                expanded = expandedCategory,
                onDismissRequest = { expandedCategory = false }
            ) {
                categories.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            category = item
                            expandedCategory = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(4.dp))
        Text("Pilihan: AC, Keran/Air, Listrik, Bangunan", style = MaterialTheme.typography.labelSmall, color = Ink400)

        Spacer(Modifier.height(18.dp))

        // Foto Fasilitas Upload Box
        Text("Foto Fasilitas", style = MaterialTheme.typography.titleSmall, color = Ink900, modifier = Modifier.entrance(4))
        Spacer(Modifier.height(6.dp))

        Surface(
            onClick = {
                photoSelected = true
                Toast.makeText(context, "Foto kerusakan dipilih dari galeri HP!", Toast.LENGTH_SHORT).show()
            },
            shape = RoundedCornerShape(14.dp),
            color = White,
            border = BorderStroke(1.dp, Line),
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .entrance(5)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(14.dp)
            ) {
                Icon(Icons.Outlined.CameraAlt, contentDescription = null, tint = Green700, modifier = Modifier.size(32.dp))
                Spacer(Modifier.height(8.dp))
                Text(
                    text = if (photoSelected) "✓ Foto Kerusakan Terpilih" else "Ketuk untuk unggah foto",
                    style = MaterialTheme.typography.titleSmall,
                    color = Green700
                )
                Text("JPG atau PNG • Maks. 5 MB", style = MaterialTheme.typography.labelSmall, color = Ink400)
            }
        }

        Spacer(Modifier.height(18.dp))

        // Deskripsi Kerusakan Input Field
        Text("Deskripsi Kerusakan", style = MaterialTheme.typography.titleSmall, color = Ink900, modifier = Modifier.entrance(6))
        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("Jelaskan kerusakan yang dialami...", color = Ink400, style = MaterialTheme.typography.bodyMedium) },
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .entrance(7),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Green700,
                unfocusedBorderColor = Line,
                focusedContainerColor = White,
                unfocusedContainerColor = White
            )
        )

        Spacer(Modifier.height(24.dp))

        PrimaryButton(
            text = "Kirim Laporan",
            loading = loading,
            onClick = {
                if (category == "Pilih kategori") {
                    Toast.makeText(context, "Mohon pilih kategori kerusakan", Toast.LENGTH_SHORT).show()
                    return@PrimaryButton
                }
                if (description.isEmpty()) {
                    Toast.makeText(context, "Mohon berikan deskripsi kerusakan", Toast.LENGTH_SHORT).show()
                    return@PrimaryButton
                }
                loading = true
                coroutineScope.launch {
                    Toast.makeText(context, "Laporan kerusakan berhasil dikirim ke Pak Abbas!", Toast.LENGTH_LONG).show()
                    loading = false
                    onSubmitted()
                }
            },
            modifier = Modifier.entrance(8)
        )
    }
}
