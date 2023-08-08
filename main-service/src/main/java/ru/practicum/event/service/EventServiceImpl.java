package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.enums.EventSortBy;
import ru.practicum.enums.States;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.DynamicEventRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.user.repository.RequestRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        if(rangeStart==null){
            rangeStart = LocalDateTime.now().minusYears(1);
        }
        if(rangeEnd==null){
            rangeEnd = LocalDateTime.now().plusYears(100);
        }
        if(rangeEnd.isBefore(rangeStart)){
            throw new BadRequestException("End date is before start date.");
        }
        String sortStr;
        if (sort.equals(EventSortBy.EVENT_DATE)){
            sortStr = "eventDate";
        }else {
            sortStr = "views";
        }
        List<EventShortDto> result = dynamicRepository.getEvents(text,categories,paid,rangeStart,rangeEnd,onlyAvailable, sortStr)
                .stream()
                .map(mapper::toShortDto)
                .collect(Collectors.toList());
        System.out.println(result);

        result.forEach(event -> event.setViews(statService.getViews(event.getId())));
        PagedListHolder page = new PagedListHolder(result);
        page.setPageSize(size);
        page.setPage(from);
        statService.addHit(request);
        return new ArrayList<>(page.getPageList());
    }

    @Override
    public EventFullDto getEventById(long eventId,HttpServletRequest request) {
        Event event = repository.getById(eventId);
        if (!event.getState().equals(States.PUBLISHED)){
            throw new NotFoundException("Published event not found.");
        }
        statService.addHit(request);
        event.setViews(statService.getViews(eventId));
        return mapper.toFullDto(event,requestRepository.getConfirmedRequestsCount(event.getId()));
    }
}
