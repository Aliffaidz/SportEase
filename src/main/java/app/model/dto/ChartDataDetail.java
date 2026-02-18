package app.model.dto;

public record ChartDataDetail(
        String label,      // Contoh: "Senin" atau "Minggu 1"
        Double percentage  // Hasil perhitungan (Booking / Kapasitas * 100)
) {}