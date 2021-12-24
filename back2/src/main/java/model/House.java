package model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "soa_house")
@XStreamAlias("house")
public class House {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String name; //Поле не может быть null

    @Min(1)
    private Integer year; //Значение поля должно быть больше 0

    @Min(1)
    @Max(75)
    private Integer numberOfFloors; //Максимальное значение поля: 75, Значение поля должно быть больше 0

    @Min(1)
    private Integer numberOfFlatsOnFloor; //Значение поля должно быть больше 0

    @Min(1)
    private Integer numberOfLifts; //Значение поля должно быть больше 0

    public void copy(House house) {
        this.name = house.name;
        this.year = house.year;
        this.numberOfFloors = house.numberOfFloors;
        this.numberOfFlatsOnFloor = house.numberOfFlatsOnFloor;
        this.numberOfLifts = house.numberOfLifts;
    }
}
