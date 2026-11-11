package com.kostku.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class UserRole { ADMIN, TENANT }

data class Session(
    val role: UserRole,
    val userId: Long,
    val name: String,
    /** Info tambahan, mis. "Kamar 102" untuk penghuni. */
    val label: String? = null
)

private val Context.sessionDataStore by preferencesDataStore(name = "kostku_session")

class SessionStore(private val context: Context) {

    private object Keys {
        val ROLE = stringPreferencesKey("role")
        val USER_ID = longPreferencesKey("user_id")
        val NAME = stringPreferencesKey("name")
        val LABEL = stringPreferencesKey("label")
    }

    val session: Flow<Session?> = context.sessionDataStore.data.map { prefs ->
        val role = prefs[Keys.ROLE]?.let { runCatching { UserRole.valueOf(it) }.getOrNull() }
        val id = prefs[Keys.USER_ID]
        val name = prefs[Keys.NAME]
        if (role != null && id != null && name != null) {
            Session(role, id, name, prefs[Keys.LABEL])
        } else null
    }

    suspend fun save(session: Session) {
        context.sessionDataStore.edit { prefs ->
            prefs[Keys.ROLE] = session.role.name
            prefs[Keys.USER_ID] = session.userId
            prefs[Keys.NAME] = session.name
            if (session.label != null) prefs[Keys.LABEL] = session.label else prefs.remove(Keys.LABEL)
        }
    }

    suspend fun clear() {
        context.sessionDataStore.edit { it.clear() }
    }
}
