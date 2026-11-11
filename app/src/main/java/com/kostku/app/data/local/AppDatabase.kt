package com.kostku.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        AdminEntity::class,
        RoomEntity::class,
        RoomPhotoEntity::class,
        TenantEntity::class,
        PaymentEntity::class,
        AnnouncementEntity::class,
        RepairReportEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun adminDao(): AdminDao
    abstract fun roomDao(): RoomDao
    abstract fun roomPhotoDao(): RoomPhotoDao
    abstract fun tenantDao(): TenantDao
    abstract fun paymentDao(): PaymentDao
    abstract fun announcementDao(): AnnouncementDao
    abstract fun repairReportDao(): RepairReportDao

    companion object {
        fun build(context: Context): AppDatabase =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "kostku.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
