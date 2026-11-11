package com.kostku.app.ui.screens.tenant

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kostku.app.ui.components.KostKuTextField
import com.kostku.app.ui.components.PrimaryButton
import com.kostku.app.ui.components.SecondaryButton
import com.kostku.app.ui.theme.Danger
import com.kostku.app.ui.theme.DangerBg
import com.kostku.app.ui.theme.Green50
import com.kostku.app.ui.theme.Green700
import com.kostku.app.ui.theme.Ink400
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900
import com.kostku.app.ui.theme.Line
import com.kostku.app.ui.theme.White

data class EditableTenantItem(
    val id: String,
    var name: String,
    val roomNumber: Int,
    var phone: String,
    var nik: String = "7371010101000101",
    var entryDate: String = "1 Januari 2024",
    var monthlyRate: Long = 800_000L
) {
    val initials: String
        get() {
            val parts = name.trim().split(" ")
            return if (parts.size >= 2) {
                "${parts[0].firstOrNull() ?: ""}${parts[1].firstOrNull() ?: ""}".uppercase()
            } else if (parts.isNotEmpty() && parts[0].isNotEmpty()) {
                "${parts[0].first()}".uppercase()
            } else "N/A"
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataPenghuniScreen(
    onBack: () -> Unit = {},
    onAddTenant: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Data Penghuni", style = MaterialTheme.typography.titleMedium, color = Ink900) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Kembali", tint = Ink900)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            DataPenghuniContent(onAddTenant = onAddTenant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataPenghuniContent(
    onAddTenant: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    val tenantsList = remember {
        mutableListOf(
            EditableTenantItem("1", "Siti Aminah", 101, "081234567801", "7371010101000101", "1 Jan 2024"),
            EditableTenantItem("2", "Andi", 102, "081234567802", "7371010101000102", "15 Feb 2024"),
            EditableTenantItem("3", "Rizky Pratama", 104, "081234567804", "7371010101000104", "10 Mar 2024"),
            EditableTenantItem("4", "Andi Saputra", 105, "081234567805", "7371010101000105", "1 Apr 2024"),
            EditableTenantItem("5", "Dewi Lestari", 106, "081234567806", "7371010101000106", "20 Mei 2024"),
            EditableTenantItem("6", "Rina Wulandari", 107, "081234567807", "7371010101000107", "1 Jun 2024"),
            EditableTenantItem("7", "Agus Setiawan", 108, "081234567808", "7371010101000108", "15 Jun 2024"),
            EditableTenantItem("8", "Nadia Putri", 109, "081234567809", "7371010101000109", "1 Jul 2024"),
            EditableTenantItem("9", "Fajar Hidayat", 110, "081234567810", "7371010101000110", "10 Ags 2024")
        )
    }

    var selectedTenantForEdit by remember { mutableStateOf<EditableTenantItem?>(null) }
    val sheetState = rememberModalBottomSheetState()

    val filteredTenants = tenantsList.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.roomNumber.toString().contains(searchQuery)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text("Penghuni New Afra Kost", style = MaterialTheme.typography.headlineMedium, color = Ink900)
        Text("Kelola identitas dan kamar penghuni.", style = MaterialTheme.typography.bodySmall, color = Ink600)
        Spacer(Modifier.height(16.dp))

        // Search Input Box
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari nama atau nomor kamar", color = Ink400, style = MaterialTheme.typography.bodyMedium) },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Ink400) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Green700,
                unfocusedBorderColor = Line,
                focusedContainerColor = White,
                unfocusedContainerColor = White
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("${filteredTenants.size} penghuni aktif", style = MaterialTheme.typography.titleSmall, color = Ink900)
            Text("Semua lantai ∨", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Green700)
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredTenants) { tenant ->
                Surface(
                    onClick = {
                        selectedTenantForEdit = tenant
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = White,
                    border = BorderStroke(1.dp, Line),
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(Green50, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tenant.initials, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = Green700)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(tenant.name, style = MaterialTheme.typography.titleSmall, color = Ink900)
                            Text("Kamar ${tenant.roomNumber} • ${tenant.phone}", style = MaterialTheme.typography.bodySmall, color = Ink600)
                        }
                        Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = Ink400)
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        PrimaryButton(
            text = "+ Tambah Penghuni Baru",
            onClick = onAddTenant
        )
    }

    // Modal Bottom Sheet: Detail & Edit Penghuni
    if (selectedTenantForEdit != null) {
        val tenant = selectedTenantForEdit!!
        var nameInput by remember { mutableStateOf(tenant.name) }
        var phoneInput by remember { mutableStateOf(tenant.phone) }
        var nikInput by remember { mutableStateOf(tenant.nik) }
        var entryDateInput by remember { mutableStateOf(tenant.entryDate) }

        ModalBottomSheet(
            onDismissRequest = { selectedTenantForEdit = null },
            sheetState = sheetState,
            containerColor = White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text("Detail & Edit Data Penghuni", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = Ink900)
                Text("Kamar ${tenant.roomNumber} • New Afra Kost", style = MaterialTheme.typography.bodySmall, color = Ink600)

                Spacer(Modifier.height(20.dp))

                KostKuTextField(
                    label = "Nama Lengkap",
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    placeholder = "Masukkan nama lengkap"
                )

                Spacer(Modifier.height(12.dp))

                KostKuTextField(
                    label = "NIK (16 Digit)",
                    value = nikInput,
                    onValueChange = { nikInput = it },
                    placeholder = "7371010101000000"
                )

                Spacer(Modifier.height(12.dp))

                KostKuTextField(
                    label = "Nomor HP / WhatsApp",
                    value = phoneInput,
                    onValueChange = { phoneInput = it },
                    placeholder = "08xx xxxx xxxx"
                )

                Spacer(Modifier.height(12.dp))

                KostKuTextField(
                    label = "Tanggal Masuk",
                    value = entryDateInput,
                    onValueChange = { entryDateInput = it },
                    placeholder = "1 Januari 2024"
                )

                Spacer(Modifier.height(20.dp))

                PrimaryButton(
                    text = "Simpan Perubahan Data",
                    onClick = {
                        tenant.name = nameInput
                        tenant.phone = phoneInput
                        tenant.nik = nikInput
                        tenant.entryDate = entryDateInput
                        Toast.makeText(context, "Data ${tenant.name} berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                        selectedTenantForEdit = null
                    }
                )

                Spacer(Modifier.height(10.dp))

                SecondaryButton(
                    text = "Batal",
                    onClick = { selectedTenantForEdit = null }
                )

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
