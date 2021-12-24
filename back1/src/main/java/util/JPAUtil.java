package util;

import model.Coordinates;
import model.Flat;
import model.House;
import model.View;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class JPAUtil {

    private static final EntityManager entityManager = createEntityManager();
    private static final HashMap<String, Function<String, Object>> possibleParams = createPossibleParams();

    private static EntityManager createEntityManager() {
        return Persistence.createEntityManagerFactory("hibernate").createEntityManager();
    }

    private static HashMap<String, Function<String, Object>> createPossibleParams() {
        HashMap<String, Function<String, Object>> result = new HashMap<>();
        result.put("id", Integer::parseInt);
        result.put("name", s -> s);
        result.put("coordinates", Coordinates::fromString);
        result.put("creationDate", s -> LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        result.put("area", Float::parseFloat);
        result.put("numberOfRooms", Long::parseLong);
        result.put("price", Double::parseDouble);
        result.put("kitchenArea", Float::parseFloat);
        result.put("view", View::valueOf);
        result.put("house", Integer::parseInt);
        return result;
    }

    public static void saveFlat(Flat flat) {
        entityManager.getTransaction().begin();
        entityManager.persist(flat);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }

    public static void saveHouse(House house) {
        entityManager.getTransaction().begin();
        entityManager.persist(house);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }

    public static FlatListWrap getFlats(RequestStructure requestStructure) {
        Map<String, String[]> params = requestStructure.getParams();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        try {
            CriteriaQuery<Flat> criteriaQuery = criteriaBuilder.createQuery(Flat.class);
            Root<Flat> flatRoot = criteriaQuery.from(Flat.class);
            //filtering
            List<Predicate> predicates = new ArrayList<>();
            for (String s : params.keySet()) {
                if (possibleParams.containsKey(s)) {
                    predicates.add(criteriaBuilder.equal(flatRoot.get(s), possibleParams.get(s).apply(params.get(s)[0])));
                }
            }
            criteriaQuery.select(flatRoot).where(predicates.toArray(new Predicate[0]));
            // sorting
            if (params.containsKey("orderBy")) {
                List<Order> orders = new ArrayList<>();
                for (String s : params.get("orderBy")) {
                    String[] order = s.split(",");
                    if (order.length != 2) {
                        throw new RuntimeException(s + ": each order parameter should have 'asc' or 'desc' value");
                    }
                    if (possibleParams.containsKey(order[0]) && order[1].equals("desc")) {
                        orders.add(criteriaBuilder.desc(flatRoot.get(order[0])));
                    } else if (possibleParams.containsKey(order[0]) && order[1].equals("asc")) {
                        orders.add(criteriaBuilder.asc(flatRoot.get(order[0])));
                    } else {
                        throw new RuntimeException(s + " is not a correct order parameter");
                    }
                }
                criteriaQuery.orderBy(orders);
            }
            // pagination
            TypedQuery<Flat> query = entityManager.createQuery(criteriaQuery);
            int countResults = query.getResultList().size();
            if (params.containsKey("pageNumber") && params.containsKey("pageSize")) {
                int pageNumber = Integer.parseInt(params.get("pageNumber")[0]);
                int perPage = Integer.parseInt(params.get("pageSize")[0]);

                if (((long) (pageNumber - 1) * perPage >= countResults && countResults > 0) || pageNumber <= 0) {
                    throw new RuntimeException("pagination out of bounds");
                }
                query.setFirstResult((pageNumber - 1) * perPage);
                query.setMaxResults(perPage);
            }
            return new FlatListWrap(query.getResultList(), countResults);
        } catch (Exception e) {
            requestStructure.setResponseCode(400);
            requestStructure.setMessage(e.getMessage());
            return null;
        }
    }

    public static Flat getFlat(Integer id) {
        return entityManager.find(Flat.class, id);
    }

    public static House getHouse(Integer id) {
        return entityManager.find(House.class, id);
    }

    public static void deleteFlat(Flat flat) {
        entityManager.getTransaction().begin();
        entityManager.remove(flat);
        entityManager.getTransaction().commit();
    }

    public static Long getNumberPriceLower(String s) {
        Double a = Double.valueOf(s);
        Query countQuery = entityManager.createQuery("select count (f) from Flat f where f.price < :num").setParameter("num", a);
        return (Long) countQuery.getSingleResult();
    }

    public static List<Flat> getNamesContain(String s) {
        return entityManager.createQuery("select f from Flat f where f.name like :str", Flat.class).setParameter("str", "%" + s + "%").getResultList();
    }

    public static List<Flat> getNamesStart(String s) {
        return entityManager.createQuery("select f from Flat f where f.name like :str", Flat.class).setParameter("str", s + "%").getResultList();
    }

}
