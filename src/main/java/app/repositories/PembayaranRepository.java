package app.repositories;

import app.entities.Pembayaran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PembayaranRepository extends JpaRepository<Pembayaran,Integer>, JpaSpecificationExecutor<Pembayaran> {
}
