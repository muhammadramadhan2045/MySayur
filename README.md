# MySayur Location Tracking App

## Demo Aplikasi
Tonton demo aplikasi di YouTube melalui tautan berikut:  
[Demo MySayur Location Tracking App](https://youtu.be/57L6LeTfJpM)

![MySayur App Demo](https://cdn.imgpile.com/f/3dW6RTY_xl.png)

**MySayur Location Tracking** adalah aplikasi yang memungkinkan pelacakan lokasi karyawan setiap 5
menit dan menyimpan data lokasi ke dalam database lokal (SQLite). Aplikasi ini juga menampilkan
history lokasi dalam bentuk list atau pin pada peta dan memiliki mekanisme untuk menghapus data
harian secara otomatis.

## Fitur Utama

1. **Pengambilan dan Penyimpanan Lokasi**:
    - Lokasi karyawan diambil setiap 5 menit.
    - Lokasi disimpan di SQLite Database.

2. **Penggunaan Library Android Jetpack**:
    - Menggunakan WorkManager untuk menghapus data harian secara otomatis setiap jam 10 malam.
    - Menampilkan notifikasi ketika data berhasil dihapus.

3. **Tampilan Data History Lokasi**:
    - Menampilkan data history lokasi dalam bentuk list dan peta (Google Maps).

4. **Pengambilan Lokasi Berkelanjutan**:
    - Aplikasi tetap melacak lokasi meskipun aplikasi terkunci, ditutup, atau dalam kondisi offline.

5. **Penghapusan Data Harian**:
    - Setiap jam 10 malam, data lokasi karyawan dihapus secara otomatis.
    - Notifikasi akan muncul saat penghapusan data berhasil.

## Struktur Proyek

- **LocationService**: Mengambil lokasi karyawan setiap 5 menit
  menggunakan `FusedLocationProviderClient` dan menyimpan lokasi ke SQLite Database.
- **WorkManager**: Menggunakan `CleanUpWorker` untuk menghapus data lokasi setiap jam 10 malam.
- **MapsFragment**: Menampilkan lokasi karyawan dalam bentuk pin di peta (Google Maps).
- **ListLocationFragment**: Menampilkan riwayat lokasi karyawan dalam bentuk list.

## Teknologi yang Digunakan

- **Kotlin**: Bahasa pemrograman yang digunakan untuk aplikasi ini.
- **Android Jetpack**:
- **WorkManager**: Untuk menjalankan pekerjaan di latar belakang secara periodik.
- **Google Maps API**: Untuk menampilkan riwayat lokasi pada peta.
- **SQLite**: Untuk menyimpan data lokasi karyawan secara lokal.
- **Koin**: Untuk dependency injection.

## Instalasi

Clone repositori ini:

bash
[git clone https://github.com/muhammadramadhan2045/MySayur.git](https://github.com/muhammadramadhan2045/MySayur.git)

untuk branch utama : jetpack_navigation

jangan lupa untuk mengisi API KEY Map pada AndroidManifest.xml di module app
