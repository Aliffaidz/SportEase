package app.repositories;

import app.entities.Facility;
import app.entities.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility,Integer> {

    @Query("SELECT f FROM Facility f LEFT JOIN FETCH f.imageFacilities")
    List<Facility> getAllFacilityWithImage();

    @Query("SELECT f FROM Facility f JOIN f.imageFacilities i WHERE f.id = :facilityId")
    Optional<Facility> getFacilityById(@Param("facilityId")Integer facilityId);

}
