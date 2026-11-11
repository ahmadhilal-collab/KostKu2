package com.kostku.app.util

object PhoneUtils {
    /** Ubah ke format lokal: 08xxxxxxxxxx (tanpa spasi, tanda hubung, atau +62). */
    fun normalize(raw: String): String {
        var digits = raw.filter { it.isDigit() }
        if (digits.startsWith("62")) digits = "0" + digits.removePrefix("62")
        return digits
    }

    /** Format internasional untuk wa.me: 628xxxxxxxxxx. */
    fun toInternational(raw: String): String {
        val local = normalize(raw)
        return if (local.startsWith("0")) "62" + local.removePrefix("0") else local
    }
}

object Validators {
    private val phoneRegex = Regex("^08[0-9]{8,12}$")

    fun isValidPhone(raw: String): Boolean = phoneRegex.matches(PhoneUtils.normalize(raw))

    fun isValidNik(raw: String): Boolean = raw.length == 16 && raw.all { it.isDigit() }
}
