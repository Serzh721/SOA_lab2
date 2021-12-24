package model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;

@Getter
@Setter
@Embeddable
@XStreamAlias("coordinates")
public class Coordinates {

    private long x;

    @Max(22)
    private double y; //Максимальное значение поля: 22

    public static Coordinates fromString(String s) {
        String[] coords = s.split(",");
        if (coords.length != 2) {
            throw new IllegalArgumentException("invalid coordinates format");
        }
        Coordinates result = new Coordinates();
        result.setX(Long.parseLong(coords[0]));
        result.setY(Double.parseDouble(coords[1]));
        return result;
    }
}