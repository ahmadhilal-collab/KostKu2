package com.kostku.app.data.repository

import com.kostku.app.BuildConfig
import com.kostku.app.data.local.AdminDao
import com.kostku.app.data.local.AdminEntity
import com.kostku.app.data.local.RoomDao
import com.kostku.app.data.local.Session
import com.kostku.app.data.local.SessionStore
import com.kostku.app.data.local.TenantDao
import com.kostku.app.data.local.UserRole
import com.kostku.app.util.PasswordHasher
import com.kostku.app.util.PhoneUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthException(message: String) : Exception(message)

class AuthRepository(
    private val adminDao: AdminDao,
    private val tenantDao: TenantDao,
    private val roomDao: RoomDao,
    private val sessionStore: SessionStore
) {

    suspend fun registerAdmin(
        name: String,
        phone: String,
        password: String,
        secretCode: String
    ): Result<Unit> = runCatching {
        if (secretCode.trim() != BuildConfig.ADMIN_SECRET_CODE) {
            throw AuthException("Sandi rahasia tidak sesuai. Hubungi pengembang aplikasi untuk mendapatkannya.")
        }
        val normalizedPhone = PhoneUtils.normalize(phone)
        if (adminDao.findByPhone(normalizedPhone) != null) {
            throw AuthException("Nomor WhatsApp ini sudah terdaftar. Silakan masuk.")
        }
        val hash = withContext(Dispatchers.Default) { PasswordHasher.hash(password) }
        val id = adminDao.insert(
            AdminEntity(name = name.trim(), phone = normalizedPhone, passwordHash = hash)
        )
        sessionStore.save(Session(UserRole.ADMIN, id, name.trim()))
    }

    suspend fun loginAdmin(identifier: String, password: String): Result<Unit> = runCatching {
        val admin = adminDao.findByPhoneOrName(PhoneUtils.normalize(identifier), identifier.trim())
        val valid = admin != null &&
            withContext(Dispatchers.Default) { PasswordHasher.verify(password, admin.passwordHash) }
        if (!valid || admin == null) {
            throw AuthException("Nama/nomor HP atau kata sandi salah.")
        }
        sessionStore.save(Session(UserRole.ADMIN, admin.id, admin.name))
    }

    suspend fun loginTenant(name: String, nik: String, phone: String): Result<Unit> = runCatching {
        val tenant = tenantDao.findActiveByNik(nik.trim())
        val matches = tenant != null &&
            tenant.name.trim().equals(name.trim(), ignoreCase = true) &&
            PhoneUtils.normalize(tenant.phone) == PhoneUtils.normalize(phone)
        if (!matches || tenant == null) {
            throw AuthException("Data tidak cocok dengan data yang dicatat pengelola. Periksa nama, NIK, dan nomor HP Anda.")
        }
        val room = roomDao.getById(tenant.roomId)
        sessionStore.save(
            Session(UserRole.TENANT, tenant.id, tenant.name, room?.let { "Kamar ${it.number}" })
        )
    }

    suspend fun logout() = sessionStore.clear()

    /** Nomor WhatsApp pengelola (admin pertama) untuk tombol "Hubungi Pak Abbas". */
    suspend fun ownerPhone(): String? = adminDao.getFirst()?.phone
}
