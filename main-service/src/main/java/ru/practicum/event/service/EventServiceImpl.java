package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.enums.EventSortBy;
import ru.practicum.enums.EventStateAction;
import ru.practicum.enums.States;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.UpdateEventAdminDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.DynamicEventRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.user.repository.RequestRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final DynamicEventRepository dynamicRepository;
    private final EventMapper mapper;
    private final RequestRepository requestRepository;
    private final StatService statService;


    @Override
    public List<EventShortDto> getEvents(String text,
                                         List<Long> categories,
                                         boolean paid,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         boolean onlyAvailable,
                                         EventSortBy sort,
                                         int from,
                                         int size,
                                         HttpServletRequest request) {
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now().minusYears(1);
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(100);
        }
        if (rangeEnd.isBefore(rangeStart)) {
            throw new BadRequestException("End date is before start date.");
        }
        String sortStr;
        if (sort.equals(EventSortBy.EVENT_DATE)) {
            sortStr = "eventDate";
        } else {
            sortStr = "views";
        }
        List<EventShortDto> result = dynamicRepository.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sortStr)
                .stream()
                .map(mapper::toShortDto)
                .collect(Collectors.toList());
        result.forEach(event -> event.setViews(statService.getViews(event.getId())));
        PagedListHolder<EventShortDto> page = new PagedListHolder<>(result);
        page.setPageSize(size);
        page.setPage(from);
        statService.addHit(request);
        return new ArrayList<>(page.getPageList());
    }

    @Override
    public EventFullDto getEventById(long eventId, HttpServletRequest request) {
        Event event = repository.getById(eventId);
        if (!event.getState().equals(States.PUBLISHED)) {
            throw new NotFoundException("Published event not found.");
        }
        statService.addHit(request);
        event.setViews(statService.getViews(eventId));
        return mapper.toFullDto(event, requestRepository.getConfirmedRequestsCount(event.getId()));
    }

    //-------------------Admin-------------------------

    @Override
    public List<EventFullDto> searchEvent(List<Long> users,
                                          List<States> states,
                                          List<Long> categories,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd,
                                          int from,
                                          int size) {
        PagedListHolder<EventFullDto> page = new PagedListHolder<>(dynamicRepository.searchEvent(users, states, categories, rangeStart, rangeEnd)
                .stream()
                .map(event -> mapper.toFullDto(event, requestRepository.getConfirmedRequestsCount(event.getId())))
                .collect(Collectors.toList()));
        page.setPageSize(size);
        page.setPage(from);
        return new ArrayList<>(page.getPageList());
    }

    @Override
    public EventFullDto updateEvent(long eventId, UpdateEventAdminDto upd) {
        Event actualEvent = repository.getById(eventId);
        if (actualEvent.getState().equals(States.PUBLISHED)) {
            throw new ConflictException("Event is published.");
        }
        if (actualEvent.getState().equals(States.CANCELED)) {
            throw new ConflictException("Event is canceled.");
        }
        if (actualEvent.getState().equals(States.REJECTED)) {
            throw new ConflictException("Event is rejected.");
        }
        if (upd.getAnnotation() != null) {
            if (upd.getAnnotation().length() < 20 || upd.getAnnotation().length() > 2000) {
                throw new BadRequestException("Annotation length is invalid");
            }
            actualEvent.setAnnotation(upd.getAnnotation());
        }
        if (upd.getDescription() != null) {
            if (upd.getDescription().length() < 20 || upd.getDescription().length() > 7000) {
                throw new BadRequestException("Description length is invalid");
            }
            actualEvent.setDescription(upd.getDescription());
        }
        if (upd.getPaid() != null) {
            actualEvent.setPaid(upd.getPaid());
        }
        if (upd.getEventDate() != null) {
            if (upd.getEventDate().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Event date is before current time.");
            }
            actualEvent.setEventDate(upd.getEventDate());
        }
        if (upd.getTitle() != null) {
            if (upd.getTitle().length() < 3 || upd.getTitle().length() > 120) {
                throw new BadRequestException("Title length is invalid");
            }
            actualEvent.setTitle(upd.getTitle());
        }
        if (upd.getParticipantLimit() > 0) {
            actualEvent.setParticipantLimit(upd.getParticipantLimit());
        }
        if (Objects.equals(upd.getStateAction(), EventStateAction.PUBLISH_EVENT)) {
            actualEvent.setPublishedOn(LocalDateTime.now());
            actualEvent.setState(States.PUBLISHED);
        }
        if (Objects.equals(upd.getStateAction(), EventStateAction.REJECT_EVENT)) {
            actualEvent.setState(States.CANCELED);
        }
        return mapper.toFullDto(repository.save(actualEvent), requestRepository.getConfirmedRequestsCount(actualEvent.getId()));
    }
}
