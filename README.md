# QuestNavigasiMVVM_0093

Aplikasi Android berbasis **Jetpack Compose** dengan pola arsitektur **MVVM (Model-View-ViewModel)** dan manajemen navigasi modern menggunakan library **navigation-compose** (`NavHost`).

---

## 📱 Ringkasan Fitur

- **Navigasi Layar Deklaratif (NavHost)**:
  - Perpindahan antar halaman secara fleksibel dan aman menggunakan `rememberNavController()`.
  - Dukungan pengiriman parameter dinamis (contoh: rute `quest_detail/{questId}`).
  - Integrasi `BackHandler` untuk penanganan gesture & tombol back sistem.
- **Pola Arsitektur MVVM**:
  - **Model**: `QuestItem`, `QuestCategory`, `QuestDifficulty`.
  - **Repository**: `QuestRepository` & `InMemoryQuestRepository` memanfaatkan reaktifitas Kotlin `Flow`.
  - **ViewModel**: `QuestViewModel` mengelola `QuestUiState` dengan `StateFlow` dan operasi asynchronous coroutine.
  - **View (UI)**: Komponen Jetpack Compose murni dengan Material Design 3.
- **Fitur Aplikasi Quest**:
  - Daftar quest interaktif dengan progress indicator dan status penyelesaian.
  - Pencarian real-time dan penyaringan berbasis tingkat kesulitan (*Easy*, *Medium*, *Hard*).
  - Layar detail quest dengan aksi hapus dan ubah status.
  - Formulir penambahan quest kustom.
  - Layar statistik dan pencapaian peringkat (*Rank* & *EXP*).

---

## 🛠️ Struktur Direktori

```
app/src/main/java/com/example/
├── MainActivity.kt               # Entry point ComponentActivity
├── AppNavigation.kt              # NavHost dan orkestrasi composable utama
├── data/
│   ├── model/
│   │   ├── QuestItem.kt          # Data class entitas Quest
│   │   └── QuestCategory.kt      # Enum kesulitan dan kategori
│   └── repository/
│       └── QuestRepository.kt    # Interface & implementasi penyimpanan
├── ui/
│   ├── navigation/
│   │   └── Screen.kt             # Definisi rute layar (Sealed class)
│   ├── screens/
│   │   ├── QuestListScreen.kt    # Layar utama daftar quest
│   │   ├── QuestDetailScreen.kt  # Layar detail quest dengan argumen
│   │   ├── AddQuestScreen.kt     # Layar form penambahan quest
│   │   └── QuestStatsScreen.kt   # Layar statistik dan level
│   ├── theme/                    # Material 3 Color, Theme, & Typography
│   └── viewmodel/
│       └── QuestViewModel.kt     # State holder dan business logic
```

---

## 🧪 Pengujian (Testing)

Proyek ini dilengkapi dengan unit test:
- `QuestRepositoryTest.kt`: Memverifikasi operasi data `getQuests()`, `toggleQuestCompletion()`, `addQuest()`, dan `deleteQuest()`.
- `QuestViewModelTest.kt`: Memverifikasi transisi state reaktif pada pencarian, filter, dan perhitungan poin.
- `ExampleRobolectricTest.kt`: Pengujian komponen lokal JVM dengan Robolectric.

Jalankan pengujian via terminal:
```bash
gradle :app:testDebugUnitTest
```

---

## 🚀 Kompilasi & Build

```bash
gradle assembleDebug
```
