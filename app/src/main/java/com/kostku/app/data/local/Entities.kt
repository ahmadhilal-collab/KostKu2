package com.kostku.app.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class RoomStatus { OCCUPIED, EMPTY, MAINTENANCE }
enum class PaymentStatus { PAID, UNPAID }
enum class ReportStatus { SENT, IN_PROGRESS, DONE }
enum class AnnouncementTarget { ALL, FLOOR_1, FLOOR_2 }

/** Akun pemilik / pengelola kost (Admin). */
@Entity(tableName = "admins", indices = [Index(value = ["phone"], unique = true)])
data class AdminEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String,
    val passwordHash: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "rooms", indices = [Index(value = ["number"], unique = true)])
data class RoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val number: Int,
    val floor: Int,
    val status: RoomStatus = RoomStatus.EMPTY,
    val monthlyRate: Long = 800_000L,
    val facilities: String = "Kasur Springbed, Lemari Pakaian, Wi-Fi, AC/Kipas, Listrik & Air",
    val maintenanceNote: String? = null
)

@Entity(
    tableName = "room_photos",
    foreignKeys = [
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("roomId")]
)
data class RoomPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val roomId: Long,
    val uri: String,
    val caption: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "tenants", indices = [Index("nik"), Index("roomId")])
data class TenantEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val nik: String,
    val phone: String,
    val roomId: Long,
    val entryDate: Long,
    val monthlyRate: Long,
    val active: Boolean = true
)

@Entity(
    tableName = "payments",
    indices = [Index(value = ["tenantId", "periodYear", "periodMonth"], unique = true)]
)
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tenantId: Long,
    val roomId: Long,
    val periodYear: Int,
    val periodMonth: Int,
    val amount: Long,
    val status: PaymentStatus = PaymentStatus.UNPAID,
    val method: String? = null,
    val paidAt: Long? = null,
    val receiptNo: String? = null,
    val receivedBy: String? = null,
    val dueDate: Long,
    val reminderSentAt: Long? = null
)

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val body: String,
    val target: AnnouncementTarget = AnnouncementTarget.ALL,
    val publishedAt: Long = System.currentTimeMillis(),
    val author: String
)

@Entity(tableName = "repair_reports", indices = [Index("tenantId")])
data class RepairReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tenantId: Long,
    val roomId: Long,
    val category: String,
    val description: String,
    val photoUri: String? = null,
    val status: ReportStatus = ReportStatus.SENT,
    val createdAt: Long = System.currentTimeMillis()
)
