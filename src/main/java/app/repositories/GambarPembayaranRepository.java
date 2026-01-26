package app.repositories;

import app.entities.GambarPembayaran;
import app.entities.Pembayaran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GambarPembayaranRepository extends JpaRepository<GambarPembayaran,String>{

    Optional<GambarPembayaran> findFirstByIdPembayaran(Pembayaran pembayaran);

}
