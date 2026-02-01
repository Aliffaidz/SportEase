package app.repositories;

import app.entities.Facility;
import app.entities.ImageFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageFacilityRepository extends JpaRepository<ImageFacility,String>{

    List<ImageFacility> findAllByFacility(Facility facility);

}
