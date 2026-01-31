package app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "gambar_lapangan")
@Data
@AllArgsConstructor
public class ImagesField extends ImageData {

    public ImagesField(){}

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "field",referencedColumnName = "id")
    private Field field;

}
