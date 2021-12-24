package model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "soa_flat")
@XStreamAlias("flat")
public class Flat {
    @Id
    @GeneratedValue
    private Integer id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @NotEmpty
    @NotNull
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull
    @Embedded
    private Coordinates coordinates; //Поле не может быть null

    @NotNull
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Min(0)
    private Float area; //Значение поля должно быть больше 0

    @Min(1)
    private Long numberOfRooms; //Значение поля должно быть больше 0

    @Min(0)
    private Double price; //Значение поля должно быть больше 0

    @Min(0)
    private Float kitchenArea; //Поле может быть null, Значение поля должно быть больше 0

    private View view; //Поле может быть null

    @NotNull
    @OneToOne(cascade = CascadeType.MERGE)
    private House house; //Поле не может быть null

    public void copy(Flat fl) {
        this.name = fl.name;
        this.coordinates = fl.coordinates;
        this.area = fl.area;
        this.numberOfRooms = fl.numberOfRooms;
        this.price = fl.price;
        this.kitchenArea = fl.kitchenArea;
        this.view = fl.view;
        this.house = fl.house;
    }
}
