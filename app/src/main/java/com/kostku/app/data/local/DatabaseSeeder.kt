package com.kostku.app.data.local

import com.kostku.app.BuildConfig
import com.kostku.app.util.PasswordHasher
import java.time.LocalDate
import java.time.ZoneId

/**
 * Mengisi data awal: 36 kamar (Lantai 1 = 16, Lantai 2 = 20) selalu dibuat.
 * Jika BuildConfig.SEED_DEMO_DATA = true, ditambah akun admin contoh, penghuni, pembayaran, dan pengumuman.
 *
 * Akun contoh:
 *  - Admin    : Pak Abbas / 081234567890 / sandi: admin123
 *  - Penghuni : Andi / NIK 7371010101000102 / HP 081234560102 (Kamar 102, belum bayar)
 */
class DatabaseSeeder(private val db: AppDatabase) {

    suspend fun seedIfEmpty() {
        if (db.roomDao().count() == 0) seedRooms()
        if (BuildConfig.SEED_DEMO_DATA && db.adminDao().count() == 0 && db.tenantDao().countAll() == 0) {
            seedDemo()
        }
    }

    private suspend fun seedRooms() {
        val rooms = buildList {
            for (i in 1..16) add(RoomEntity(number = 100 + i, floor = 1))
            for (i in 1..20) add(RoomEntity(number = 200 + i, floor = 2))
        }
        db.roomDao().insertAll(rooms)
    }

    private suspend fun seedDemo() {
        val zone = ZoneId.systemDefault()
        fun millis(date: LocalDate, hour: Int = 10) =
            date.atTime(hour, 0).atZone(zone).toInstant().toEpochMilli()

        db.adminDao().insert(
            AdminEntity(
                name = "Pak Abbas",
                phone = "081234567890",
                passwordHash = PasswordHasher.hash("admin123")
            )
        )

        val floor1 = mapOf(
            101 to "Siti Aminah", 102 to "Andi", 104 to "Rizky Pratama", 105 to "Andi Saputra",
            106 to "Dewi Lestari", 107 to "Rina Wulandari", 108 to "Agus Setiawan",
            109 to "Nadia Putri", 110 to "Fajar Hidayat", 111 to "Intan Permata",
            112 to "Dimas Maulana", 113 to "Lina Marlina", 114 to "Yoga Firmansyah",
            115 to "Fitri Handayani", 116 to "Arif Rahman"
        )
        val floor2Names = listOf(
            "Budi Santoso", "Citra Ayu", "Eko Prasetyo", "Gita Savitri", "Hendra Wijaya",
            "Indah Permatasari", "Joko Susilo", "Kartika Sari", "Lukman Hakim", "Maya Anggraini",
            "Naufal Rizki", "Olivia Putri", "Putra Mahendra", "Qori Amalia", "Rudi Hartono",
            "Sinta Maharani", "Taufik Hidayat", "Umar Faruq", "Vina Oktaviani"
        )
        // Kamar 103 dan 215 dalam perbaikan
        val floor2Rooms = (201..220).filter { it != 215 }
        val floor2 = floor2Rooms.zip(floor2Names).toMap()
        val residents = floor1 + floor2

        val unpaidThisMonth = setOf(102, 104, 109, 114, 203, 209, 212)
        val today = LocalDate.now()
        val thisMonth = today.withDayOfMonth(1)

        for ((number, name) in residents) {
            val room = db.roomDao().getByNumber(number) ?: continue
            val suffix = number.toString().padStart(4, '0')
            val tenantId = db.tenantDao().insert(
                TenantEntity(
                    name = name,
                    nik = "737101010100$suffix",
                    phone = "08123456$suffix",
                    roomId = room.id,
                    entryDate = millis(thisMonth.minusMonths(6)),
                    monthlyRate = 800_000L
                )
            )
            db.roomDao().update(room.copy(status = RoomStatus.OCCUPIED))

            // 3 bulan sebelumnya lunas, bulan ini sesuai daftar
            for (offset in 3L downTo 0L) {
                val period = thisMonth.minusMonths(offset)
                val paid = offset > 0 || number !in unpaidThisMonth
                val paidDate = period.withDayOfMonth(1 + (number % 5))
                db.paymentDao().insert(
                    PaymentEntity(
                        tenantId = tenantId,
                        roomId = room.id,
                        periodYear = period.year,
                        periodMonth = period.monthValue,
                        amount = 800_000L,
                        status = if (paid) PaymentStatus.PAID else PaymentStatus.UNPAID,
                        method = if (paid) "Tunai" else null,
                        paidAt = if (paid) millis(paidDate) else null,
                        receiptNo = if (paid) "NK-%d%02d-%d".format(period.year, period.monthValue, number) else null,
                        receivedBy = if (paid) "Pak Abbas" else null,
                        dueDate = millis(period.withDayOfMonth(10), 23)
                    )
                )
            }
        }

        listOf(103, 215).forEach { number ->
            db.roomDao().getByNumber(number)?.let {
                db.roomDao().update(
                    it.copy(status = RoomStatus.MAINTENANCE, maintenanceNote = "Perbaikan fasilitas")
                )
            }
        }

        val now = System.currentTimeMillis()
        val day = 24L * 60 * 60 * 1000
        db.announcementDao().insertAll(
            listOf(
                AnnouncementEntity(
                    title = "Pembersihan area bersama",
                    body = "Minggu, 8 November, pukul 08.00-10.00. Mohon simpan barang pribadi agar kegiatan berjalan lancar. Terima kasih.",
                    publishedAt = now - 2 * day,
                    author = "Pak Abbas"
                ),
                AnnouncementEntity(
                    title = "Jaga kenyamanan bersama",
                    body = "Mohon kurangi kebisingan setelah pukul 22.00 dan selalu kunci pintu utama saat keluar masuk.",
                    publishedAt = now - 10 * day,
                    author = "Pak Abbas"
                )
            )
        )
    }
}
