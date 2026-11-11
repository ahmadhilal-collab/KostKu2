package com.kostku.app.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.Locale

object ReceiptExporter {

    fun generateReceiptBitmap(
        receiptNo: String,
        tenantName: String,
        roomNumber: Int,
        periodStr: String,
        paidDateStr: String,
        method: String,
        amount: Long,
        receivedBy: String
    ): Bitmap {
        val width = 1080
        val height = 1420
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Background
        canvas.drawColor(Color.parseColor("#F5F7FA"))

        // Card Container
        val cardPaint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        val cardBorderPaint = Paint().apply {
            color = Color.parseColor("#E2E8F0")
            style = Paint.Style.STROKE
            strokeWidth = 3f
            isAntiAlias = true
        }
        val cardRect = RectF(60f, 60f, (width - 60).toFloat(), (height - 60).toFloat())
        canvas.drawRoundRect(cardRect, 36f, 36f, cardPaint)
        canvas.drawRoundRect(cardRect, 36f, 36f, cardBorderPaint)

        val textPaint = Paint().apply {
            isAntiAlias = true
            color = Color.parseColor("#0F2A20")
        }

        // Header Title: KostKu
        textPaint.apply {
            textSize = 56f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            color = Color.parseColor("#166A4A")
        }
        canvas.drawText("KostKu", 120f, 150f, textPaint)

        textPaint.apply {
            textSize = 36f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            color = Color.parseColor("#0F2A20")
        }
        canvas.drawText("New Afra Kost", 120f, 210f, textPaint)

        textPaint.apply {
            textSize = 28f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            color = Color.parseColor("#5B6B63")
        }
        canvas.drawText("Jl. Perintis Kemerdekaan 4", 120f, 255f, textPaint)

        textPaint.apply {
            textSize = 26f
            color = Color.parseColor("#93A09A")
        }
        canvas.drawText("Nota: $receiptNo", 120f, 295f, textPaint)

        // Divider Line
        val linePaint = Paint().apply {
            color = Color.parseColor("#E2E8F0")
            strokeWidth = 3f
        }
        canvas.drawLine(120f, 330f, (width - 120).toFloat(), 330f, linePaint)

        // Table Rows
        val labelPaint = Paint().apply {
            isAntiAlias = true
            textSize = 30f
            color = Color.parseColor("#5B6B63")
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        val valuePaint = Paint().apply {
            isAntiAlias = true
            textSize = 32f
            color = Color.parseColor("#0F2A20")
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        fun drawRow(y: Float, label: String, value: String) {
            canvas.drawText(label, 120f, y, labelPaint)
            val valWidth = valuePaint.measureText(value)
            canvas.drawText(value, (width - 120 - valWidth), y, valuePaint)
        }

        val floorStr = if (roomNumber < 200) "1" else "2"
        drawRow(410f, "Nama penghuni", tenantName)
        drawRow(480f, "Kamar", "$roomNumber • Lantai $floorStr")
        drawRow(550f, "Periode sewa", periodStr)
        drawRow(620f, "Tanggal bayar", paidDateStr)
        drawRow(690f, "Metode", method)

        canvas.drawLine(120f, 740f, (width - 120).toFloat(), 740f, linePaint)

        // Total Paid & Status Badge
        canvas.drawText("Total dibayar (All-In)", 120f, 810f, labelPaint)

        // Stamp Lunas Box
        val stampBgPaint = Paint().apply {
            color = Color.parseColor("#F0F7F3")
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        val stampRect = RectF((width - 320).toFloat(), 770f, (width - 120).toFloat(), 830f)
        canvas.drawRoundRect(stampRect, 16f, 16f, stampBgPaint)

        val stampTextPaint = Paint().apply {
            color = Color.parseColor("#166A4A")
            textSize = 32f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText("LUNAS", (width - 275).toFloat(), 812f, stampTextPaint)

        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val priceText = formatRupiah.format(amount).replace(",00", "")

        val totalPaint = Paint().apply {
            color = Color.parseColor("#0F2A20")
            textSize = 64f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText(priceText, 120f, 900f, totalPaint)

        textPaint.apply {
            textSize = 28f
            color = Color.parseColor("#5B6B63")
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        canvas.drawText("Delapan ratus ribu rupiah", 120f, 950f, textPaint)

        canvas.drawLine(120f, 1020f, (width - 120).toFloat(), 1020f, linePaint)

        textPaint.apply {
            textSize = 28f
            color = Color.parseColor("#166A4A")
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText("Diterima oleh: $receivedBy", 120f, 1100f, textPaint)

        textPaint.apply {
            textSize = 24f
            color = Color.parseColor("#93A09A")
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        canvas.drawText("Diterbitkan secara sah oleh Sistem KostKu New Afra Kost", 120f, 1240f, textPaint)

        return bitmap
    }

    fun saveReceiptImage(context: Context, bitmap: Bitmap, receiptNo: String): File? {
        return try {
            val dir = File(context.cacheDir, "receipts").apply { mkdirs() }
            val file = File(dir, "Nota_$receiptNo.png")
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun shareReceiptImage(context: Context, file: File, receiptNo: String) {
        val uri: Uri = FileProvider.getUriForFile(context, "com.kostku.app.fileprovider", file)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Bagikan Nota Digital $receiptNo"))
    }
}
