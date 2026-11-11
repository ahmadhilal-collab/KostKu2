package com.kostku.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kostku.app.KostKuApp
import com.kostku.app.data.repository.AuthRepository
import com.kostku.app.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object AuthField {
    const val IDENTIFIER = "identifier"
    const val NAME = "name"
    const val PHONE = "phone"
    const val NIK = "nik"
    const val PASSWORD = "password"
    const val SECRET = "secret"
}

data class AuthUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
    val success: Boolean = false,
    val ownerPhone: String? = null
)

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui.asStateFlow()

    fun clearFieldError(field: String) {
        _ui.update { it.copy(fieldErrors = it.fieldErrors - field, error = null) }
    }

    fun loginAdmin(identifier: String, password: String) {
        val errors = buildMap {
            if (identifier.isBlank()) put(AuthField.IDENTIFIER, "Nama atau nomor HP wajib diisi")
            if (password.isBlank()) put(AuthField.PASSWORD, "Kata sandi wajib diisi")
        }
        if (errors.isNotEmpty()) return setErrors(errors)
        submit { repository.loginAdmin(identifier, password) }
    }

    fun registerAdmin(name: String, phone: String, password: String, secret: String) {
        val errors = buildMap {
            if (name.trim().length < 3) put(AuthField.NAME, "Nama lengkap minimal 3 huruf")
            if (!Validators.isValidPhone(phone)) put(AuthField.PHONE, "Nomor WhatsApp tidak valid (contoh: 0812xxxxxxxx)")
            if (password.length < 6) put(AuthField.PASSWORD, "Kata sandi minimal 6 karakter")
            if (secret.isBlank()) put(AuthField.SECRET, "Sandi rahasia wajib diisi")
        }
        if (errors.isNotEmpty()) return setErrors(errors)
        submit { repository.registerAdmin(name, phone, password, secret) }
    }

    fun loginTenant(name: String, nik: String, phone: String) {
        val errors = buildMap {
            if (name.trim().length < 3) put(AuthField.NAME, "Nama sesuai identitas wajib diisi")
            if (!Validators.isValidNik(nik)) put(AuthField.NIK, "NIK harus 16 digit angka")
            if (!Validators.isValidPhone(phone)) put(AuthField.PHONE, "Nomor HP tidak valid (contoh: 0812xxxxxxxx)")
        }
        if (errors.isNotEmpty()) return setErrors(errors)
        submit { repository.loginTenant(name, nik, phone) }
    }

    fun loadOwnerPhone() {
        viewModelScope.launch {
            val phone = repository.ownerPhone()
            _ui.update { it.copy(ownerPhone = phone) }
        }
    }

    private fun setErrors(errors: Map<String, String>) {
        _ui.update { it.copy(fieldErrors = errors, error = null) }
    }

    private fun submit(block: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _ui.update { it.copy(loading = true, error = null, fieldErrors = emptyMap()) }
            block()
                .onSuccess { _ui.update { s -> s.copy(loading = false, success = true) } }
                .onFailure { e ->
                    _ui.update { s ->
                        s.copy(loading = false, error = e.message ?: "Terjadi kesalahan. Coba lagi.")
                    }
                }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as KostKuApp
                AuthViewModel(app.container.authRepository)
            }
        }
    }
}
