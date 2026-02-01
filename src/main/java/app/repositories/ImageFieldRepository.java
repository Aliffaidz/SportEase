package app.repositories;

import app.entities.Field;
import app.entities.ImagesField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageFieldRepository extends JpaRepository<ImagesField,String> {

    List<ImagesField> findAllByField(Field field);

}
