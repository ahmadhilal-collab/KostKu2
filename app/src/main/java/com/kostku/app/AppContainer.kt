package com.kostku.app

import android.content.Context
import com.kostku.app.data.local.AppDatabase
import com.kostku.app.data.local.DatabaseSeeder
import com.kostku.app.data.local.SessionStore
import com.kostku.app.data.repository.AdminRepository
import com.kostku.app.data.repository.AuthRepository
import com.kostku.app.data.repository.RoomRepository
import com.kostku.app.data.repository.TenantRepository

/** Dependency container sederhana (tanpa Hilt) supaya mudah dipahami. */
class AppContainer(context: Context) {
    val database: AppDatabase = AppDatabase.build(context)
    val sessionStore = SessionStore(context)
    val seeder = DatabaseSeeder(database)

    val authRepository = AuthRepository(
        adminDao = database.adminDao(),
        tenantDao = database.tenantDao(),
        roomDao = database.roomDao(),
        sessionStore = sessionStore
    )
    val roomRepository = RoomRepository(database.roomDao())
    val adminRepository = AdminRepository(database)
    val tenantRepository = TenantRepository(database)
}
