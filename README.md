# 🧺 Harf Tepsisi - Ömer Asaf Okuma Arkadaşı

İlkokul 1. sınıf öğrencisi **Ömer Asaf** için özel olarak hazırlanmış, ses temelli okuma ve hızlı okuma pratik uygulaması.

---

## 🚀 Özellikler

- **Müfredat:** 2024 MEB Ses Grubu: **A - N - E - T - İ - L** (ANETİL).
- **10 Saniyelik Akıllı Sayaç:** Çocukların okuma hızını artırmak ve motive etmek için renkli geri sayım sayacı.
- **Aralıklı Tekrar (Spaced Repetition):** 10 saniye içinde okunmayan kelimeler silinmez, 3-4 kelime sonra tekrar ekrana gelerek pekiştirilir.
- **Büyük Punto & Heceleme:** Ekranın tam ortasında büyük punto ve istenirse tek dokunuşla renkli heceleme (`an - ne`, `e - la`, `at - let`).
- **Sesli Okuma (TTS):** Takıldığı kelimeleri dinleyebilmesi için Türkçe sesli okuma desteği.
- **Hızlı Okuma Piramitleri:** Çalışma yapraklarındaki piramit cümle okumaları (*Ela al*, *Talat tatili anlat*, *İnan attan in* vb.).
- **Yıldız & Konfeti Ödülü:** Her doğru okumada konfeti ve ses efekti ile motivasyon artırıcı yıldız sistemi.

---

## 🔤 Yeni Harf Nasıl Eklenir?

Ömer Asaf yeni harfler öğrendikçe (örn. *O, K, U, R, I, M, S* vb.):
1. `index.html` dosyasındaki `activeLetters` kümesine yeni harfi ekleyin:
   ```javascript
   let activeLetters = new Set(['a', 'n', 'e', 't', 'i', 'l', 'yeni_harf']);
   ```
2. `MASTER_DICTIONARY` dizisine yeni harfle yapılabilecek anlamlı kelimeleri heceleriyle ekleyin.
3. Değişiklikleri GitHub'a gönderin (push).

---

## 📦 Otomatik APK Üretimi (GitHub Actions)

Bu depoda GitHub Actions entegrasyonu mevcuttur:
1. Depoya her `push` yapıldığında veya **Actions** sekmesinden **Run workflow** dendiğinde GitHub sunucuları otomatik olarak Android APK'sını derler.
2. Derlenen APK, **Actions > En son işlem > Artifacts** bölümünden doğrudan Android telefonunuza indirilip kurulabilir (`HarfTepsisi-OmerAsaf.apk`).

---

## 📲 PWA Olarak Telefona Ekleme (APK'sız Hızlı Yükleme)

Tarayıcıdan açıldığında Chrome menüsünden **"Ana Ekrana Ekle"** veya **"Uygulamayı Yükle"** butonuna basıldığında telefona yerel uygulama gibi kurulur, internet olmadan da tam ekran çalışır.
