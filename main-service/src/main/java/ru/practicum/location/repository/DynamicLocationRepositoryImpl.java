package ru.practicum.location.repository;

import org.springframework.stereotype.Component;
import ru.practicum.enums.LocationStatuses;
import ru.practicum.enums.LocationTypes;
import ru.practicum.location.model.Location;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class DynamicLocationRepositoryImpl implements DynamicLocationRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Location> search(String name,
                                 String country,
                                 LocationStatuses status,
                                 LocationTypes type) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Location> query = builder.createQuery(Location.class);
        Root<Location> root = query.from(Location.class);
        Predicate criteria = builder.conjunction();
        if (name != null && !name.isBlank()) {
            Predicate namePredicate = builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
            criteria = builder.and(criteria, namePredicate);
        }
        if (country != null && !country.isBlank()) {
            Predicate countryPredicate = builder.like(builder.lower(root.get("country")), "%" + country.toLowerCase() + "%");
            criteria = builder.and(criteria, countryPredicate);
        }
        if (status != null) {
            criteria = builder.and(criteria, root.get("status").in(status));
        }
        if (type != null) {
            criteria = builder.and(criteria, root.get("type").in(type));
        }
        query.select(root).where(criteria);
        return entityManager.createQuery(query).getResultList();
    }
}
