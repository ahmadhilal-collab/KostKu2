# KostKu - Milestone 1 (Fondasi + Alur Masuk)

Aplikasi Android (Kotlin, Jetpack Compose, Material 3, MVVM + Repository, Room, DataStore) untuk New Afra Kost.

## Cara menjalankan
1. Buka Android Studio (Ladybug 2024.2.1 atau lebih baru) -> File > Open -> pilih folder `KostKu`.
2. Tunggu Gradle Sync selesai (butuh internet untuk mengunduh dependency).
3. Pilih emulator (API 26+) atau HP asli, lalu klik Run.

## Akun contoh (data demo)
- Admin: `Pak Abbas` atau `081234567890`, sandi `admin123`
- Penghuni: nama `Andi`, NIK `7371010101000102`, HP `081234560102` (Kamar 102)
- Kode daftar admin baru (Sandi Rahasia): `NEWAFRA2026` (ubah di app/build.gradle.kts)

## Struktur
- data/local: entity, DAO, database Room, session DataStore, seeder data demo
- data/repository: AuthRepository, RoomRepository
- ui/theme: warna (dari logo), tipografi Inter, bentuk
- ui/components: komponen bersama (field, tombol, kartu peran, animasi)
- ui/screens/auth: splash, onboarding, pilih peran, login/daftar admin, login penghuni
- ui/navigation: NavHost + transisi animasi
