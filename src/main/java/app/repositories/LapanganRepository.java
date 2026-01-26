package app.repositories;

import app.entities.Lapangan;
import app.entities.enums.StatusLapangan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LapanganRepository extends JpaRepository<Lapangan,Integer> {

    @Query("SELECT l FROM Lapangan l JOIN FETCH l.gambarLapangan WHERE l.id = :id")
    Optional<Lapangan> ambilSemuaDataDanGambar(@Param("id") Integer  id);

    @Query("SELECT COUNT(l) FROM Lapangan l WHERE l.statusLapangan = :status")
    long totalLapanganAktif(@Param("status") StatusLapangan statusLapangan);

    @Query("SELECT l FROM Lapangan l WHERE l.statusLapangan = :status")
    List<Lapangan> ambilLapanganBerdasarkanStatus(@Param("status") StatusLapangan statusLapangan);

    @Query("SELECT l.namaLapangan FROM Lapangan l")
    List<String> ambiNamaLapangan();

}
