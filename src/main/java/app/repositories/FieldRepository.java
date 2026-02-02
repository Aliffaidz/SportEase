package app.repositories;

import app.entities.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FieldRepository extends JpaRepository<Field,Integer> {

    @Query("SELECT f FROM Field f LEFT JOIN FETCH f.imagesFieldList")
    List<Field> findAllFieldsWithImages();

    @Query("SELECT f FROM Field f JOIN f.imagesFieldList i WHERE f.id = :fieldId")
    Optional<Field> getFieldById(@Param("fieldId")Integer fieldId);

    @Query("SELECT f FROM Field f WHERE f.id = :idField")
    Optional<Field> getOperationalHour(@Param("idField")Integer idField);

}
