package app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "rekening_pembayaran")
@AllArgsConstructor
public class RekeningPembayaran {

    @Id
    private String bankName;

    @Column(name = "atas_nama",nullable = false)
    private String userName;

    @Column(name = "no_rekening",nullable = false)
    private String noRekening;

}
