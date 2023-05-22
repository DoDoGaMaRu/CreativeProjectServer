package persistence.entity;

import lombok.*;
import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long rcpSeq;
    private String rcpJson;
    private Float infoCar;
    private Float infoPro;
    private Float infoFat;
    private Float infoNa;
}