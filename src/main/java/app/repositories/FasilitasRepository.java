package app.repositories;

import app.entities.Fasilitas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FasilitasRepository extends JpaRepository<Fasilitas,Integer> {


}
