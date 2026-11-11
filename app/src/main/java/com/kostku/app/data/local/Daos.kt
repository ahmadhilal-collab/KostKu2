package com.kostku.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(admin: AdminEntity): Long

    @Query("SELECT * FROM admins WHERE phone = :phone OR LOWER(name) = LOWER(:name) LIMIT 1")
    suspend fun findByPhoneOrName(phone: String, name: String): AdminEntity?

    @Query("SELECT * FROM admins WHERE phone = :phone LIMIT 1")
    suspend fun findByPhone(phone: String): AdminEntity?

    @Query("SELECT * FROM admins ORDER BY id ASC LIMIT 1")
    suspend fun getFirst(): AdminEntity?

    @Query("SELECT COUNT(*) FROM admins")
    suspend fun count(): Int
}

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(rooms: List<RoomEntity>): List<Long>

    @Update
    suspend fun update(room: RoomEntity)

    @Query("SELECT * FROM rooms ORDER BY number ASC")
    fun observeAll(): Flow<List<RoomEntity>>

    @Query("SELECT * FROM rooms WHERE id = :id")
    fun observeById(id: Long): Flow<RoomEntity?>

    @Query("SELECT * FROM rooms WHERE id = :id")
    suspend fun getById(id: Long): RoomEntity?

    @Query("SELECT * FROM rooms WHERE number = :number LIMIT 1")
    suspend fun getByNumber(number: Int): RoomEntity?

    @Query("SELECT * FROM rooms WHERE status = 'EMPTY' ORDER BY number ASC")
    fun observeAvailable(): Flow<List<RoomEntity>>

    @Query("SELECT COUNT(*) FROM rooms")
    suspend fun count(): Int

    @Query("SELECT COUNT(*) FROM rooms WHERE status = :status")
    fun observeCount(status: RoomStatus): Flow<Int>
}

@Dao
interface RoomPhotoDao {
    @Insert
    suspend fun insert(photo: RoomPhotoEntity): Long

    @Query("SELECT * FROM room_photos WHERE roomId = :roomId ORDER BY createdAt ASC")
    fun observeByRoom(roomId: Long): Flow<List<RoomPhotoEntity>>

    @Query("DELETE FROM room_photos WHERE id = :id")
    suspend fun delete(id: Long)
}

@Dao
interface TenantDao {
    @Insert
    suspend fun insert(tenant: TenantEntity): Long

    @Update
    suspend fun update(tenant: TenantEntity)

    @Query("SELECT * FROM tenants WHERE id = :id")
    suspend fun getById(id: Long): TenantEntity?

    @Query("SELECT * FROM tenants WHERE id = :id")
    fun observeById(id: Long): Flow<TenantEntity?>

    @Query("SELECT * FROM tenants WHERE active = 1 ORDER BY name ASC")
    fun observeActive(): Flow<List<TenantEntity>>

    @Query("SELECT * FROM tenants WHERE nik = :nik AND active = 1 LIMIT 1")
    suspend fun findActiveByNik(nik: String): TenantEntity?

    @Query("SELECT * FROM tenants WHERE roomId = :roomId AND active = 1 LIMIT 1")
    suspend fun findActiveByRoom(roomId: Long): TenantEntity?

    @Query("SELECT COUNT(*) FROM tenants")
    suspend fun countAll(): Int
}

@Dao
interface PaymentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(payment: PaymentEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(payments: List<PaymentEntity>)

    @Update
    suspend fun update(payment: PaymentEntity)

    @Query("SELECT * FROM payments WHERE id = :id")
    suspend fun getById(id: Long): PaymentEntity?

    @Query("SELECT * FROM payments WHERE tenantId = :tenantId AND periodYear = :year AND periodMonth = :month LIMIT 1")
    suspend fun find(tenantId: Long, year: Int, month: Int): PaymentEntity?

    @Query("SELECT * FROM payments WHERE periodYear = :year AND periodMonth = :month")
    fun observeByPeriod(year: Int, month: Int): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE tenantId = :tenantId ORDER BY periodYear DESC, periodMonth DESC")
    fun observeByTenant(tenantId: Long): Flow<List<PaymentEntity>>
}

@Dao
interface AnnouncementDao {
    @Insert
    suspend fun insert(announcement: AnnouncementEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(list: List<AnnouncementEntity>)

    @Query("SELECT * FROM announcements ORDER BY publishedAt DESC")
    fun observeAll(): Flow<List<AnnouncementEntity>>

    @Query("DELETE FROM announcements WHERE id = :id")
    suspend fun delete(id: Long)
}

@Dao
interface RepairReportDao {
    @Insert
    suspend fun insert(report: RepairReportEntity): Long

    @Update
    suspend fun update(report: RepairReportEntity)

    @Query("SELECT * FROM repair_reports ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<RepairReportEntity>>

    @Query("SELECT * FROM repair_reports WHERE tenantId = :tenantId ORDER BY createdAt DESC")
    fun observeByTenant(tenantId: Long): Flow<List<RepairReportEntity>>
}
