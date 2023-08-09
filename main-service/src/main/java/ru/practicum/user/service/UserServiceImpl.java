package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.enums.EventStateAction;
import ru.practicum.enums.States;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.user.dto.event.NewEventDto;
import ru.practicum.user.dto.event.UpdateEventUserRequest;
import ru.practicum.user.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.user.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.user.dto.request.ParticipationRequestDto;
import ru.practicum.user.mapper.RequestMapper;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.Request;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.RequestRepository;
import ru.practicum.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    private final CategoryRepository categoryRepository;

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final LocationRepository locationRepository;
    //-------------Event-Requests--------------------

    @Override
    public List<ParticipationRequestDto> getUsersRequestsInfo(long userId) {
        return requestRepository.getAllByUserId(userId)
                .stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto sendEventRequest(long userId, long eventId) {
        if (!requestRepository.getAllByUserIdAndEventId(userId, eventId).isEmpty()) {
            throw new ConflictException("This user has already sent a request for this event.");
        }
        EventFullDto event = eventMapper.toFullDto(eventRepository.getById(eventId), requestRepository.getConfirmedRequestsCount(eventId));
        if (userId == event.getInitiator().getId()) {
            throw new ConflictException("This user is initiator of the event.");
        }
        if (event.getState() != States.PUBLISHED) {
            throw new ConflictException("This event is not published.");
        }
        if (event.getParticipantLimit() != 0 &&
                event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("This event has reached its participant limit.");
        }

        Request request = new Request();
        request.setEvent(eventId);
        request.setRequester(userId);
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            request.setStatus(States.CONFIRMED);
        } else {
            request.setStatus(States.PENDING);
        }
        request.setCreatedOn(LocalDateTime.now());
        return requestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelEventRequest(long userId, long requestId) {
        Request actualRequest = requestRepository.getById(requestId);
        actualRequest.setStatus(States.CANCELED);
        return requestMapper.toDto(requestRepository.save(actualRequest));
    }
    //---------------Events-------------------------

    @Override
    public List<EventShortDto> getOwnedEvents(long userId, int from, int size) {
        PagedListHolder page = new PagedListHolder(eventRepository.findAllByInitiatorId(userId)
                .stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList()));
        page.setPageSize(size);
        page.setPage(from);
        return new ArrayList<>(page.getPageList());
    }

    @Override
    @Transactional
    public EventFullDto addEvent(long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Event date is before current time.");
        }
        if (newEventDto.getRequestModeration() == null) {
            newEventDto.setRequestModeration(true);
        }
        Category category = categoryRepository.getById(newEventDto.getCategory());
        User initiator = repository.getById(userId);
        Event event = eventMapper.toEntity(newEventDto, category, initiator);
        locationRepository.save(event.getLocation());
        event.setState(States.PENDING);
        return eventMapper.toFullDto(eventRepository.save(event), requestRepository.getConfirmedRequestsCount(event.getId()));
    }

    @Override
    public EventFullDto getEventInfo(long userId, long eventId) {
        return eventMapper.toFullDto(eventRepository.getById(eventId), requestRepository.getConfirmedRequestsCount(eventId));
    }

    @Override
    public EventFullDto updateOwnedEvent(long userId, long eventId, UpdateEventUserRequest updBody) {
        Event actualEvent = eventRepository.getById(eventId);
        if (actualEvent.getState().equals(States.PUBLISHED)) {
            throw new ConflictException("This event is published.");
        }
        if (updBody.getAnnotation() != null) {
            if (updBody.getAnnotation().length() < 20 || updBody.getAnnotation().length() > 2000) {
                throw new BadRequestException("Annotation length is invalid.");
            }
            actualEvent.setAnnotation(updBody.getAnnotation());
        }
        if (updBody.getDescription() != null) {
            if (updBody.getDescription().length() < 20 || updBody.getDescription().length() > 7000) {
                throw new BadRequestException("Description length is invalid.");
            }
            actualEvent.setDescription(updBody.getDescription());
        }
        if (updBody.getEventDate() != null) {
            if (updBody.getEventDate().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Event date is before current time.");
            }
            actualEvent.setEventDate(updBody.getEventDate());
        }
        if (updBody.getTitle() != null) {
            if (updBody.getTitle().length() < 3 || updBody.getTitle().length() > 120) {
                throw new BadRequestException("Title length is invalid.");
            }
            actualEvent.setTitle(updBody.getTitle());
        }
        if (updBody.getParticipantLimit() != null) {
            actualEvent.setParticipantLimit(updBody.getParticipantLimit());
        }
        if (updBody.getPaid() != null) {
            actualEvent.setPaid(updBody.getPaid());
        }
        if (updBody.getCategory() != null) {
            Category category = categoryRepository.getById(updBody.getCategory());
            actualEvent.setCategory(category);
        }
        if (updBody.getStateAction() != null && updBody.getStateAction().equals(EventStateAction.CANCEL_REVIEW)) {
            actualEvent.setState(States.CANCELED);
        } else {
            actualEvent.setState(States.PENDING);
        }
        return eventMapper.toFullDto(eventRepository.save(actualEvent), requestRepository.getConfirmedRequestsCount(actualEvent.getId()));
    }

    @Override
    public List<ParticipationRequestDto> getEventRequestsInfo(long userId, long eventId) {
        return requestRepository.getEventRequestsInfo(eventId)
                .stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequestsStatus(long userId, long eventId, EventRequestStatusUpdateRequest updRequest) {
        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();
        EventFullDto event = eventMapper.toFullDto(eventRepository.getById(eventId), requestRepository.getConfirmedRequestsCount(eventId));
        for (Long id : updRequest.getRequestIds()) {
            Request request = requestRepository.getById(id);
            if (Objects.equals(updRequest.getStatus(), States.CONFIRMED)) {
                if (event.getConfirmedRequests() + confirmed.size() >= event.getParticipantLimit()) {
                    throw new ConflictException("This event has reached its participant limit.");
                }
                request.setStatus(updRequest.getStatus());
                requestRepository.save(request);
                confirmed.add(requestMapper.toDto(request));
            }
            if (Objects.equals(updRequest.getStatus(), States.REJECTED)) {
                if (request.getStatus().equals(States.CONFIRMED)) {
                    throw new ConflictException("This request is confirmed. You can't reject it.");
                }
                request.setStatus(updRequest.getStatus());
                requestRepository.save(request);
                rejected.add(requestMapper.toDto(request));
            }
        }
        return requestMapper.toResultDto(confirmed, rejected);
    }
}
