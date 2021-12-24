package util;

import model.Coordinates;
import model.Flat;
import model.House;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Objects;
import java.util.Set;

public class ValidationUtil {

    private static Validator validator = setupValidator();

    private static Validator setupValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static String validate(Flat flat) {
        StringBuilder result = new StringBuilder();
        Set<ConstraintViolation<Flat>> flatViolations = validator.validate(flat);
        for (ConstraintViolation<Flat> cv : flatViolations) {
            result.append("Flat ").append(cv.getPropertyPath()).append(" ").append(cv.getMessage()).append(";");
        }
        if (!Objects.isNull(flat.getHouse())) {
            Set<ConstraintViolation<House>> houseViolations = validator.validate(flat.getHouse());
            for (ConstraintViolation<House> cv : houseViolations) {
                result.append("House ").append(cv.getPropertyPath()).append(" ").append(cv.getMessage()).append(";");
            }
        }
        if (!Objects.isNull(flat.getCoordinates())) {
            Set<ConstraintViolation<Coordinates>> coordViolations = validator.validate(flat.getCoordinates());
            for (ConstraintViolation<Coordinates> cv : coordViolations) {
                result.append("Coordinates ").append(cv.getPropertyPath()).append(" ").append(cv.getMessage()).append(";");
            }
        }
        return result.toString();
    }
}
