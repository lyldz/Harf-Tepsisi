# 🧺 Harf Tepsisi - Ömer Asaf Okuma Arkadaşı

İlkokul 1. sınıf öğrencisi **Ömer Asaf** için özel olarak hazırlanmış, ses temelli okuma ve hızlı okuma pratik uygulaması.

---

## 🚀 Özellikler (v1.0.9)

- **Müfredat Uyumu (MEB 2024 Türkiye Yüzyılı Maarif Modeli):**
  - **1. Grup:** `A - N - E - T - İ - L` (ANETİL)
  - **2. Grup:** `O - K - U - R - I - M` (OKURIM)
  - **3. Grup:** `Ü - S ...`
  - Varsayılan olarak **O** harfine kadar olan küme aktiftir (`A, N, E, T, İ, L, O`). İstenildiği an tek dokunuşla **K, U, R, I, M, Ü** harfleri aktif edilip egzersizlere dahil edilebilir.
- **10 Saniyelik Akıllı Sayaç:** Çocukların okuma hızını artırmak ve motive etmek için renkli geri sayım sayacı.
- **Aralıklı Tekrar (Spaced Repetition):** 10 saniye içinde okunmayan kelimeler silinmez, 3-4 kelime sonra tekrar ekrana gelerek pekiştirilir.
- **Büyük Punto & Renkli Heceleme:** Ekranın tam ortasında büyük punto ve istenirse tek dokunuşla renkli heceleme (`ka - le`, `o - kul`, `el - ma`).
- **Sesli Okuma (TTS):** Takıldığı kelimeleri dinleyebilmesi için Türkçe sesli okuma desteği.
- **Hızlı Okuma Piramitleri:** Çalışma yapraklarındaki piramit cümle okumaları (*Ela al*, *Talat tatili anlat*, *Kenan iki kale kur*, *Utku okula git*, *Rana tarak al* vb.).
- **Yıldız & Konfeti Ödülü:** Her doğru okumada konfeti ve ses efekti ile motivasyon artırıcı yıldız sistemi.
- **📲 Uygulama İçi Canlı Güncelleme (Tam Vakti Deneyimi):**
  - Uygulama her açılışında arka planda GitHub üzerinden güncellemeleri sessizce denetler.
  - Yeni sürüm çıktığında eğlenceli **"🎉 MÜJDE ÖMER ASAF! 🚀"** kutlama penceresi açılır.
  - Uygulama içinden canlı yüzde çubuğuyla (`%0` - `%100`) doğrudan indirilip Android paket yükleyicisiyle tek tıkla kurulabilir.

---

## 🔤 Tepsideki Harfleri Değiştirme

Uygulama içerisindeki **"✏️ Harf Seç"** butonuna basarak:
- **📘 ANETİLO (Varsayılan):** Sadece O harfine kadar olan kelimeler
- **🚀 2. Grup (+K, U, R, I, M):** 2. grup harfleriyle türetilen zengin kelimeler
- **✨ 6 Yeni Harf (+K, U, R, I, M, Ü):** Maarif modelinin 6 yeni harfini kapsayan tam pratik
- İstenilen harfler harf tablosundan tek tek açılıp kapatılabilir.

---

## 📦 Otomatik APK Üretimi (GitHub Actions)

Bu depoda GitHub Actions entegrasyonu mevcuttur:
1. Depoya her `push` yapıldığında veya `v*` etiketi yayınlandığında GitHub sunucuları otomatik olarak Android APK'sını derler.
2. Derlenen APK, **Releases** bölümünden ve uygulama içindeki canlı güncelleme sistemi üzerinden doğrudan telefona kurulabilir (`HarfTepsisi-OmerAsaf-v1.0.9.apk`).

---

## 📲 PWA Olarak Telefona Ekleme (APK'sız Hızlı Yükleme)

Tarayıcıdan açıldığında Chrome menüsünden **"Ana Ekrana Ekle"** veya **"Uygulamayı Yükle"** butonuna basıldığında telefona yerel uygulama gibi kurulur, internet olmadan da tam ekran çalışır.
