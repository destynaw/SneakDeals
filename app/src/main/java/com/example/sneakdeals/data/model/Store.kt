package com.example.sneakdeals.data.model

import androidx.annotation.DrawableRes
import com.example.sneakdeals.R

// Data class untuk informasi toko
data class Store(
    val id: String,
    val name: String,
    val mall: String, // Misal: "@Grand Indonesia"
    val address: String,
    val city: String,
    val hours: String,
    val phone: String,
    val promotions: List<String>,
    @DrawableRes val imageRes: Int
)

// Data Lengkap Toko Puma di Indonesia
val allStores = listOf(
    Store(
        id = "puma_gi",
        name = "Puma Store Grand Indonesia",
        mall = "@Grand Indonesia",
        address = "Grand Indonesia Mall, Lantai 2, Jl. MH Thamrin No.1",
        city = "Kota: Jakarta Pusat",
        hours = "Jam Buka: Setiap Hari: 10:00 - 22:00",
        phone = "Telp: 021-23580888",
        promotions = listOf("Diskon 20% untuk semua item running!", "Beli 2 gratis 1 untuk aksesoris"),
        imageRes = R.drawable.store1
    ),
    Store(
        id = "puma_sc",
        name = "Puma Store Senayan City",
        mall = "@Senayan City",
        address = "Senayan City Mall, Lantai 3, Jl. Asia Afrika Lot.19",
        city = "Kota: Jakarta Selatan",
        hours = "Jam Buka: Setiap Hari: 10:00 - 22:00",
        phone = "Telp: 021-72781030",
        promotions = listOf("Cashback 100rb setiap pembelian sepatu lifestyle"),
        imageRes = R.drawable.store2
    ),
    Store(
        id = "puma_pvj",
        name = "Puma Store Paris Van Java",
        mall = "@Paris Van Java",
        address = "Paris Van Java Mall, Resort Level, Jl. Sukajadi No.137-139",
        city = "Kota: Bandung",
        hours = "Jam Buka: Setiap Hari: 10:00 - 22:00",
        phone = "Telp: 022-82063500",
        promotions = listOf("Diskon Pelajar 15% (tunjukkan kartu pelajar)"),
        imageRes = R.drawable.store1
    ),
    Store(
        id = "puma_cp",
        name = "Puma Store Central Park",
        mall = "@Central Park",
        address = "Central Park Mall, Lantai 1, Jl. Let. Jend. S. Parman Kav. 28",
        city = "Kota: Jakarta Barat",
        hours = "Jam Buka: Setiap Hari: 10:00 - 22:00",
        phone = "Telp: 021-56985731",
        promotions = listOf(),
        imageRes = R.drawable.store2
    ),
    Store(
        id = "puma_tp",
        name = "Puma Store Tunjungan Plaza",
        mall = "@Tunjungan Plaza",
        address = "Tunjungan Plaza 4, Lantai 3, Jl. Basuki Rahmat No.8-12",
        city = "Kota: Surabaya",
        hours = "Jam Buka: Setiap Hari: 10:00 - 22:00",
        phone = "Telp: 031-5356987",
        promotions = listOf("Gratis kaos kaki setiap pembelian di atas 1jt"),
        imageRes = R.drawable.store1
    ),
    Store(
        id = "puma_beachwalk",
        name = "Puma Store Beachwalk",
        mall = "@Beachwalk Shopping Center",
        address = "Beachwalk Shopping Center, Lantai 2, Jl. Pantai Kuta",
        city = "Kota: Badung, Bali",
        hours = "Jam Buka: Setiap Hari: 10:30 - 22:30",
        phone = "Telp: 0361-8464888",
        promotions = listOf("Item baru koleksi Musim Panas tersedia"),
        imageRes = R.drawable.store2
    ),
    Store(
        id = "puma_delipark",
        name = "Puma Store Delipark Medan",
        mall = "@Delipark Mall",
        address = "Delipark Mall, Lantai UG, Jl. Putri Hijau No.1",
        city = "Kota: Medan",
        hours = "Jam Buka: Setiap Hari: 10:00 - 22:00",
        phone = "Telp: 061-42008815",
        promotions = listOf(),
        imageRes = R.drawable.store1
    ),
    Store(
        id = "puma_amplaz",
        name = "Puma Store Ambarrukmo Plaza",
        mall = "@Ambarrukmo Plaza",
        address = "Plaza Ambarrukmo, Lantai GF, Jl. Laksda Adisucipto",
        city = "Kota: Yogyakarta",
        hours = "Jam Buka: Setiap Hari: 10:00 - 22:00",
        phone = "Telp: 0274-4331326",
        promotions = listOf("Diskon khusus member MAP Club"),
        imageRes = R.drawable.store2
    ),
    Store(
        id = "puma_paragon",
        name = "Puma Store Paragon Mall Semarang",
        mall = "@Paragon Mall Semarang",
        address = "Paragon City Mall, Lantai 1, Jl. Pemuda No.118",
        city = "Kota: Semarang",
        hours = "Jam Buka: Setiap Hari: 10:00 - 22:00",
        phone = "Telp: 024-86579111",
        promotions = listOf(),
        imageRes = R.drawable.store1
    ),
    Store(
        id = "puma_pim",
        name = "Puma Store Pondok Indah Mall",
        mall = "@Pondok Indah Mall",
        address = "Pondok Indah Mall 2, Lantai 1, Jl. Metro Pondok Indah",
        city = "Kota: Jakarta Selatan",
        hours = "Jam Buka: Setiap Hari: 10:00 - 22:00",
        phone = "Telp: 021-75920831",
        promotions = listOf("Koleksi Motorsport terbaru telah tiba!"),
        imageRes = R.drawable.store2
    ),
    Store(
        id = "puma_tsm_makassar",
        name = "Puma Store Trans Studio Mall Makassar",
        mall = "@TSM Makassar",
        address = "Trans Studio Mall, Lantai 1, Jl. H.M Daeng Patompo",
        city = "Kota: Makassar",
        hours = "Jam Buka: Setiap Hari: 10:00 - 22:00",
        phone = "Telp: 0411-8117188",
        promotions = listOf(),
        imageRes = R.drawable.store1
    )
)
