package com.kostku.app.data.repository

import com.kostku.app.data.local.AnnouncementDao
import com.kostku.app.data.local.AnnouncementEntity
import com.kostku.app.data.local.AppDatabase
import com.kostku.app.data.local.PaymentDao
import com.kostku.app.data.local.PaymentEntity
import com.kostku.app.data.local.PaymentStatus
import com.kostku.app.data.local.RepairReportDao
import com.kostku.app.data.local.RepairReportEntity
import com.kostku.app.data.local.ReportStatus
import com.kostku.app.data.local.RoomDao
import com.kostku.app.data.local.RoomEntity
import com.kostku.app.data.local.RoomPhotoDao
import com.kostku.app.data.local.RoomPhotoEntity
import com.kostku.app.data.local.RoomStatus
import com.kostku.app.data.local.TenantDao
import com.kostku.app.data.local.TenantEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

data class RoomWithTenant(
    val room: RoomEntity,
    val tenant: TenantEntity?,
    val currentPayment: PaymentEntity?
)

class AdminRepository(private val db: AppDatabase) {

    fun observeAllRooms(): Flow<List<RoomEntity>> = db.roomDao().observeAll()

    fun observeActiveTenants(): Flow<List<TenantEntity>> = db.tenantDao().observeActive()

    fun observeAnnouncements(): Flow<List<AnnouncementEntity>> = db.announcementDao().observeAll()

    fun observeRepairReports(): Flow<List<RepairReportEntity>> = db.repairReportDao().observeAll()

    suspend fun getRoomByNumber(number: Int): RoomEntity? = db.roomDao().getByNumber(number)

    suspend fun getTenantByRoom(roomId: Long): TenantEntity? = db.tenantDao().findActiveByRoom(roomId)

    suspend fun getLatestPayment(tenantId: Long): PaymentEntity? {
        val today = LocalDate.now()
        return db.paymentDao().find(tenantId, today.year, today.monthValue)
    }

    suspend fun updateRoomStatus(number: Int, newStatus: RoomStatus, maintenanceNote: String? = null): Boolean {
        val room = db.roomDao().getByNumber(number) ?: return false
        val updated = room.copy(status = newStatus, maintenanceNote = maintenanceNote)
        db.roomDao().update(updated)

        if (newStatus == RoomStatus.EMPTY) {
            val tenant = db.tenantDao().findActiveByRoom(room.id)
            if (tenant != null) {
                db.tenantDao().update(tenant.copy(active = false))
            }
        }
        return true
    }

    suspend fun recordCashPayment(number: Int, receivedBy: String = "Pak Abbas"): PaymentEntity? {
        val room = db.roomDao().getByNumber(number) ?: return null
        val tenant = db.tenantDao().findActiveByRoom(room.id) ?: return null
        val today = LocalDate.now()

        var payment = db.paymentDao().find(tenant.id, today.year, today.monthValue)
        val receiptNo = "NK-%d%02d-%d".format(today.year, today.monthValue, number)
        val nowMillis = System.currentTimeMillis()

        if (payment != null) {
            val updated = payment.copy(
                status = PaymentStatus.PAID,
                method = "Tunai",
                paidAt = nowMillis,
                receiptNo = receiptNo,
                receivedBy = receivedBy
            )
            db.paymentDao().update(updated)
            payment = updated
        } else {
            val newPayment = PaymentEntity(
                tenantId = tenant.id,
                roomId = room.id,
                periodYear = today.year,
                periodMonth = today.monthValue,
                amount = room.monthlyRate,
                status = PaymentStatus.PAID,
                method = "Tunai",
                paidAt = nowMillis,
                receiptNo = receiptNo,
                receivedBy = receivedBy,
                dueDate = nowMillis
            )
            val id = db.paymentDao().insert(newPayment)
            payment = newPayment.copy(id = id)
        }

        // Update room status to OCCUPIED (Lunas)
        db.roomDao().update(room.copy(status = RoomStatus.OCCUPIED))
        return payment
    }

    suspend fun addTenant(name: String, nik: String, phone: String, roomNumber: Int, monthlyRate: Long = 800_000L): Boolean {
        val room = db.roomDao().getByNumber(roomNumber) ?: return false
        val nowMillis = System.currentTimeMillis()

        val tenant = TenantEntity(
            name = name,
            nik = nik,
            phone = phone,
            roomId = room.id,
            entryDate = nowMillis,
            monthlyRate = monthlyRate
        )
        val tenantId = db.tenantDao().insert(tenant)

        // Mark room occupied
        db.roomDao().update(room.copy(status = RoomStatus.OCCUPIED))

        // Create current month unpaid bill
        val today = LocalDate.now()
        db.paymentDao().insert(
            PaymentEntity(
                tenantId = tenantId,
                roomId = room.id,
                periodYear = today.year,
                periodMonth = today.monthValue,
                amount = monthlyRate,
                status = PaymentStatus.UNPAID,
                dueDate = nowMillis + 7L * 24 * 3600 * 1000
            )
        )
        return true
    }

    suspend fun addAnnouncement(title: String, body: String, targetStr: String, author: String = "Pak Abbas"): Long {
        val target = when (targetStr) {
            "Lantai 1" -> com.kostku.app.data.local.AnnouncementTarget.FLOOR_1
            "Lantai 2" -> com.kostku.app.data.local.AnnouncementTarget.FLOOR_2
            else -> com.kostku.app.data.local.AnnouncementTarget.ALL
        }
        val entity = AnnouncementEntity(
            title = title,
            body = body,
            target = target,
            author = author
        )
        return db.announcementDao().insert(entity)
    }

    suspend fun addRoomPhoto(roomId: Long, uri: String, caption: String): Long {
        return db.roomPhotoDao().insert(RoomPhotoEntity(roomId = roomId, uri = uri, caption = caption))
    }

    fun observeRoomPhotos(roomId: Long): Flow<List<RoomPhotoEntity>> = db.roomPhotoDao().observeByRoom(roomId)
}

class TenantRepository(private val db: AppDatabase) {

    fun observeAnnouncements(): Flow<List<AnnouncementEntity>> = db.announcementDao().observeAll()

    fun observePaymentHistory(tenantId: Long): Flow<List<PaymentEntity>> = db.paymentDao().observeByTenant(tenantId)

    fun observeRepairReports(tenantId: Long): Flow<List<RepairReportEntity>> = db.repairReportDao().observeByTenant(tenantId)

    suspend fun submitRepairReport(tenantId: Long, roomId: Long, category: String, description: String, photoUri: String? = null): Long {
        val report = RepairReportEntity(
            tenantId = tenantId,
            roomId = roomId,
            category = category,
            description = description,
            photoUri = photoUri,
            status = ReportStatus.SENT
        )
        val reportId = db.repairReportDao().insert(report)

        // Mark room as maintenance
        val room = db.roomDao().getById(roomId)
        if (room != null) {
            db.roomDao().update(room.copy(status = RoomStatus.MAINTENANCE, maintenanceNote = "$category: $description"))
        }
        return reportId
    }

    suspend fun getRoom(roomId: Long): RoomEntity? = db.roomDao().getById(roomId)
}
