package service;

import model.Flat;
import model.House;
import util.*;

import java.time.LocalDateTime;
import java.util.Objects;

public class FlatService {

    private Flat getFlatFromDB(RequestStructure requestStructure) {
        if (Objects.isNull(requestStructure.getId())) {
            requestStructure.setResponseCode(400);
            requestStructure.setMessage("id is incorrect");
            return null;
        }
        Flat flat = JPAUtil.getFlat(requestStructure.getId());
        if (Objects.isNull(flat)) {
            requestStructure.setResponseCode(404);
            requestStructure.setMessage("object not found");
            return null;
        }
        return flat;
    }

    private Flat getFlatFromRequest(RequestStructure requestStructure) {
        if (Objects.isNull(requestStructure.getRequestBody()) || requestStructure.getRequestBody().length() == 0) {
            requestStructure.setResponseCode(400);
            requestStructure.setMessage("empty body");
            return null;
        }
        Flat flat = XStreamUtil.fromXML(requestStructure);
        if (Objects.isNull(flat)) {
            requestStructure.setResponseCode(400);
            return null;
        }
        return flat;
    }

    public void getFlatList(RequestStructure requestStructure) {
        FlatListWrap flats = JPAUtil.getFlats(requestStructure);
        if (Objects.isNull(flats)) {
            return;
        }
        requestStructure.setMessage(XStreamUtil.toXML(flats));
        requestStructure.setResponseCode(200);
    }

    public void getFlat(RequestStructure requestStructure) {
        Flat flat = getFlatFromDB(requestStructure);
        if (Objects.isNull(flat)) {
            return;
        }
        requestStructure.setMessage(XStreamUtil.toXML(flat));
        requestStructure.setResponseCode(200);
    }

    public void addFlat(RequestStructure requestStructure) {
        Flat flat = getFlatFromRequest(requestStructure);
        if (Objects.isNull(flat)) {
            return;
        }
        flat.setCreationDate(LocalDateTime.now());
        flat.setId(null);
        String validationErrors = ValidationUtil.validate(flat);
        if (validationErrors.length() != 0) {
            requestStructure.setResponseCode(400);
            requestStructure.setMessage(validationErrors);
            return;
        }
        if (!Objects.isNull(flat.getHouse())) {
            JPAUtil.saveHouse(flat.getHouse());
        }
        JPAUtil.saveFlat(flat);
        requestStructure.setMessage(XStreamUtil.toXML(flat));
        requestStructure.setResponseCode(200);
    }

    public void deleteFlat(RequestStructure requestStructure) {
        Flat flat = getFlatFromDB(requestStructure);
        if (Objects.isNull(flat)) {
            return;
        }
        JPAUtil.deleteFlat(flat);
        requestStructure.setMessage("");
        requestStructure.setResponseCode(200);
    }

    public void modifyFlat(RequestStructure requestStructure) {
        Flat oldFlat = getFlatFromDB(requestStructure);
        if (Objects.isNull(oldFlat)) {
            return;
        }
        Flat newFlat = getFlatFromRequest(requestStructure);
        if (Objects.isNull(newFlat)) {
            return;
        }
        newFlat.setCreationDate(LocalDateTime.now());
        String validationErrors = ValidationUtil.validate(newFlat);
        if (validationErrors.length() != 0) {
            requestStructure.setResponseCode(400);
            requestStructure.setMessage(validationErrors);
            return;
        }
        if (!Objects.isNull(newFlat.getHouse())) {
            House newHouse = newFlat.getHouse();
            if (Objects.isNull(newHouse.getId())) {
                JPAUtil.saveHouse(newHouse);
            } else {
                House oldHouse = JPAUtil.getHouse(newHouse.getId());
                if (Objects.isNull(oldHouse)) {
                    requestStructure.setResponseCode(404);
                    requestStructure.setMessage("house not found");
                    return;
                }
                oldHouse.copy(newHouse);
                JPAUtil.saveHouse(oldHouse);
            }
        }
        oldFlat.copy(newFlat);
        JPAUtil.saveFlat(oldFlat);
        requestStructure.setMessage(XStreamUtil.toXML(oldFlat));
        requestStructure.setResponseCode(200);
    }
}
