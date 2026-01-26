package app.models.pembayaran;

import jakarta.validation.constraints.NotNull;

public record RequestAddPaymentMethod(
        @NotNull(message = "bank atau E-wallet tidak boleh kososng")
        String bankNameOrEwallet,
        @NotNull(message = "atas nama, tidak boleh kosong")
        String atasNama,
        @NotNull(message = "rekening tujuan tidak boleh kosong")
        String noRekening
) {
}
