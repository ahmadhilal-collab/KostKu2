package com.kostku.app.ui.screens.payment

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kostku.app.ui.components.KostKuLogo
import com.kostku.app.ui.components.PrimaryButton
import com.kostku.app.ui.components.SecondaryButton
import com.kostku.app.ui.components.entrance
import com.kostku.app.ui.theme.Green50
import com.kostku.app.ui.theme.Green700
import com.kostku.app.ui.theme.Ink400
import com.kostku.app.ui.theme.Ink600
import com.kostku.app.ui.theme.Ink900
import com.kostku.app.ui.theme.Line
import com.kostku.app.ui.theme.White
import com.kostku.app.util.ReceiptExporter
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotaPembayaranScreen(
    roomNumber: Int,
    tenantName: String = "Siti Aminah",
    periodStr: String = "November 2026",
    paidDateStr: String = "1 November 2026",
    amount: Long = 800_000L,
    method: String = "Tunai",
    receivedBy: String = "Pak Abbas",
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val receiptNo = "NK-202611-$roomNumber"

    val actualTenantName = when (roomNumber) {
        101 -> "Siti Aminah"
        102 -> "Andi"
        104 -> "Rizky Pratama"
        105 -> "Andi Saputra"
        106 -> "Dewi Lestari"
        107 -> "Rina Wulandari"
        108 -> "Agus Setiawan"
        109 -> "Nadia Putri"
        110 -> "Fajar Hidayat"
        else -> tenantName
    }

    val formatRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    val priceText = formatRupiah.format(amount).replace(",00", "")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nota Pembayaran Digital", style = MaterialTheme.typography.titleMedium, color = Ink900) },
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
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Success Checkmark Banner
            Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = Green700, modifier = Modifier.size(48.dp).entrance(0))
            Spacer(Modifier.height(8.dp))
            Text("Pembayaran Lunas", style = MaterialTheme.typography.headlineMedium, color = Ink900, modifier = Modifier.entrance(1))
            Text("Terima kasih, pembayaran telah dikonfirmasi.", style = MaterialTheme.typography.bodySmall, color = Ink600, modifier = Modifier.entrance(2))

            Spacer(Modifier.height(20.dp))

            // Digital Receipt Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = White,
                border = BorderStroke(1.dp, Line),
                shadowElevation = 3.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .entrance(3)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        KostKuLogo(size = 28.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("KostKu", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = Green700)
                    }

                    Spacer(Modifier.height(8.dp))
                    Text("New Afra Kost", style = MaterialTheme.typography.titleSmall, color = Ink900)
                    Text("Jl. Perintis Kemerdekaan 4", style = MaterialTheme.typography.bodySmall, color = Ink600)
                    Text("Nota: $receiptNo", style = MaterialTheme.typography.labelSmall, color = Ink400)

                    Spacer(Modifier.height(14.dp))
                    HorizontalDivider(color = Line)
                    Spacer(Modifier.height(14.dp))

                    ReceiptRow("Nama penghuni", actualTenantName)
                    ReceiptRow("Kamar", "$roomNumber • Lantai ${if (roomNumber < 200) 1 else 2}")
                    ReceiptRow("Periode sewa", periodStr)
                    ReceiptRow("Tanggal bayar", paidDateStr)
                    ReceiptRow("Metode", method)

                    Spacer(Modifier.height(14.dp))
                    HorizontalDivider(color = Line)
                    Spacer(Modifier.height(14.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Total dibayar (All-In)", style = MaterialTheme.typography.bodySmall, color = Ink600)
                        Surface(shape = RoundedCornerShape(6.dp), color = Green50) {
                            Text("LUNAS", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Green700, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(priceText, style = MaterialTheme.typography.headlineMedium, color = Ink900)
                    Text("Delapan ratus ribu rupiah", style = MaterialTheme.typography.bodySmall, color = Ink600)

                    Spacer(Modifier.height(12.dp))
                    Text("Diterima oleh: $receivedBy • Pengelola", style = MaterialTheme.typography.labelSmall, color = Ink400)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Unduh Nota Digital (Bitmap Image File Generation)
            PrimaryButton(
                text = "Unduh Nota Digital (PNG/PDF)",
                onClick = {
                    val bitmap = ReceiptExporter.generateReceiptBitmap(
                        receiptNo = receiptNo,
                        tenantName = actualTenantName,
                        roomNumber = roomNumber,
                        periodStr = periodStr,
                        paidDateStr = paidDateStr,
                        method = method,
                        amount = amount,
                        receivedBy = receivedBy
                    )
                    val savedFile = ReceiptExporter.saveReceiptImage(context, bitmap, receiptNo)
                    if (savedFile != null) {
                        Toast.makeText(context, "Nota Digital Nota_$receiptNo.png berhasil disimpan di memori HP!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Gagal mengunduh file nota", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.entrance(4)
            )

            Spacer(Modifier.height(10.dp))

            // Bagikan Nota (Image File Sharing via FileProvider)
            SecondaryButton(
                text = "Bagikan Nota File Gambar",
                onClick = {
                    val bitmap = ReceiptExporter.generateReceiptBitmap(
                        receiptNo = receiptNo,
                        tenantName = actualTenantName,
                        roomNumber = roomNumber,
                        periodStr = periodStr,
                        paidDateStr = paidDateStr,
                        method = method,
                        amount = amount,
                        receivedBy = receivedBy
                    )
                    val savedFile = ReceiptExporter.saveReceiptImage(context, bitmap, receiptNo)
                    if (savedFile != null) {
                        ReceiptExporter.shareReceiptImage(context, savedFile, receiptNo)
                    } else {
                        Toast.makeText(context, "Gagal membagikan file nota", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.entrance(5)
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Ink600)
        Text(value, style = MaterialTheme.typography.titleSmall, color = Ink900)
    }
}
