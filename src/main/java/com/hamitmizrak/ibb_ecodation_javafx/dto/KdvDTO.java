package com.hamitmizrak.ibb_ecodation_javafx.dto;

import lombok.*;
import java.time.LocalDate;

/**
 * 📦 KDV Hesaplama Veri Transfer Objesi (DTO)
 * Tutar, oran, fiş bilgileri ve hesaplama sonuçlarını kapsar.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class KdvDTO {

    // 🔢 Veritabanı için benzersiz ID
    private Integer id;

    // 💰 KDV'siz tutar (ana para)
    private Double amount;

    // 📈 Uygulanan KDV oranı (% olarak)
    private Double kdvRate;

    // 🧮 Hesaplanan KDV tutarı (amount * kdvRate / 100)
    private Double kdvAmount;

    // 💳 Genel toplam (amount + kdvAmount)
    private Double totalAmount;

    // 🧾 Fatura veya fiş numarası
    private String receiptNumber;

    // 📅 İşlem tarihi
    private LocalDate transactionDate;

    // 🗒️ İsteğe bağlı açıklama (not, kategori vs.)
    private String description;

    // 📤 Kaydın dışa aktarılma durumu (TXT, PDF, EXCEL gibi)
    private String exportFormat;

    // ✅ Geçerlilik kontrolü (istersen GUI tarafında validation için kullanılabilir)
    public boolean isValid() {
        return amount != null && kdvRate != null && amount > 0 && kdvRate >= 0 && transactionDate != null;
    }

    // 🔁 Otomatik hesaplama (veritabanına kaydetmeden önce)
    public void calculateTotals() {
        this.kdvAmount = amount * kdvRate / 100;
        this.totalAmount = amount + this.kdvAmount;
    }

    // 📄 Dışa aktarım için sade metin formatı (TXT veya PDF çıktısı için)
    public String toExportString() {
        return String.format("""
                Fiş No     : %s
                Tarih      : %s
                Açıklama   : %s
                Tutar      : %.2f ₺
                KDV Oranı  : %% %.1f
                KDV Tutarı : %.2f ₺
                Genel Toplam: %.2f ₺
                """,
                receiptNumber,
                transactionDate,
                description != null ? description : "-",
                amount,
                kdvRate,
                kdvAmount,
                totalAmount
        );
    }
}
