package app.repositories;

import app.entities.GambarLapangan;
import app.entities.Lapangan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GambarLapanganRepository extends JpaRepository<GambarLapangan,String> {

    List<GambarLapangan> findAllByLapanganGambar(Lapangan lapangan);


}
