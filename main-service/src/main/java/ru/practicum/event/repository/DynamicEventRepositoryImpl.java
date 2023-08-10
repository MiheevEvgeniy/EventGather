package ru.practicum.event.repository;

import org.springframework.stereotype.Component;
import ru.practicum.enums.States;
import ru.practicum.event.model.Event;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DynamicEventRepositoryImpl implements DynamicEventRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Event> searchEvent(List<Long> users, List<States> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        Predicate criteria = builder.conjunction();
        if (users != null && users.get(0) != 0) {
            criteria = builder.and(criteria, root.get("initiator").in(users));
        }
        if (states != null) {
            criteria = builder.and(criteria, root.get("state").in(states));
        }
        if (categories != null && categories.get(0) != 0) {
            criteria = builder.and(criteria, root.get("category").in(categories));
        }
        if (rangeStart != null) {
            criteria = builder.and(criteria, builder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        }
        if (rangeEnd != null) {
            criteria = builder.and(criteria, builder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }
        query.select(root).where(criteria);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Event> getEvents(String text, List<Long> categories, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable, String sort) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        Predicate criteria = builder.conjunction();
        if (text != null && !text.isBlank()) {
            Predicate annotation = builder.like(builder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%");
            Predicate description = builder.like(builder.lower(root.get("description")), "%" + text.toLowerCase() + "%");
            criteria = builder.and(criteria, builder.or(annotation, description));
        }
        if (categories != null && categories.get(0) != 0) {
            criteria = builder.and(criteria, root.get("category").in(categories));
        }
        if (rangeStart != null) {
            criteria = builder.and(criteria, builder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        }
        if (rangeEnd != null) {
            criteria = builder.and(criteria, builder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }
        query.select(root).where(criteria).orderBy(builder.asc(root.get(sort)));
        return entityManager.createQuery(query).getResultList();
    }

}
