package app.repositories;

import app.entities.Fasilitas;
import app.entities.GambarFasilitas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GambarFasilitasRepository extends JpaRepository<GambarFasilitas,String> {


    List<GambarFasilitas> findAllByFasilitas(Fasilitas fasilitas);

}
