package ru.practicum.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.admin.dto.event.UpdateEventAdminDto;
import ru.practicum.admin.dto.user.NewUserRequest;
import ru.practicum.admin.dto.user.UserDto;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.enums.EventStateAction;
import ru.practicum.enums.States;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.DynamicEventRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.RequestRepository;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private final EventRepository eventRepository;
    private final DynamicEventRepository dynamicEventRepository;
    private final EventMapper eventMapper;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RequestRepository requestRepository;

    //-------------Compilations------------------
    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null) {
            for (Long id : newCompilationDto.getEvents()) {
                events.add(eventRepository.getById(id));
            }
        }
        return compilationMapper.toDto(
                compilationRepository.save(
                        compilationMapper.toEntity(newCompilationDto, events)));
    }

    @Override
    public void deleteCompilation(long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(UpdateCompilationRequest updComp, long compId) {
        Compilation actualCompilation = compilationRepository.getById(compId);
        List<Event> events = new ArrayList<>();
        if (updComp.getEvents() != null) {
            for (Long id : updComp.getEvents()) {
                events.add(eventRepository.getById(id));
            }
        }
        Compilation updCompilation = compilationMapper.toEntity(updComp, events);
        if (updCompilation.getEvents() != null) {
            actualCompilation.setEvents(updCompilation.getEvents());
        }
        if (updCompilation.isPinned() != actualCompilation.isPinned()) {
            actualCompilation.setPinned(updCompilation.isPinned());
        }
        if (updCompilation.getTitle() != null) {
            if (updCompilation.getTitle().length() > 50) {
                throw new BadRequestException("Compilation title is invalid");
            }
            actualCompilation.setTitle(updCompilation.getTitle());
        }
        return compilationMapper.toDto(compilationRepository.save(actualCompilation));
    }

    //-------------Categories------------------
    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        return categoryMapper.toDto(
                categoryRepository.save(categoryMapper.toEntity(newCategoryDto)));
    }

    @Override
    public void deleteCategory(long catId) {
        Category category = categoryRepository.getById(catId);
        if (category == null) {
            throw new NotFoundException("Category with id =" + catId + " was not found");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(long catId, CategoryDto body) {
        Category actualCategory = categoryRepository.getById(catId);
        Category updCategory = categoryMapper.toEntity(body);
        if (!updCategory.getName().equals(actualCategory.getName())) {
            if (updCategory.getName().length() > 50) {
                throw new BadRequestException("Name of category is invalid");
            }
            actualCategory.setName(updCategory.getName());
        }
        return categoryMapper.toDto(categoryRepository.save(actualCategory));
    }

    //-------------Events------------------
    @Override
    public List<EventFullDto> searchEvent(List<Long> users,
                                          List<States> states,
                                          List<Long> categories,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd,
                                          int from,
                                          int size) {
        PagedListHolder page = new PagedListHolder(dynamicEventRepository.searchEvent(users, states, categories, rangeStart, rangeEnd)
                .stream()
                .map(event -> eventMapper.toFullDto(event, requestRepository.getConfirmedRequestsCount(event.getId())))
                .collect(Collectors.toList()));
        page.setPageSize(size);
        page.setPage(from);
        return new ArrayList<>(page.getPageList());
    }

    @Override
    public EventFullDto updateEvent(long eventId, UpdateEventAdminDto upd) {
        Event actualEvent = eventRepository.getById(eventId);
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
        return eventMapper.toFullDto(eventRepository.save(actualEvent), requestRepository.getConfirmedRequestsCount(actualEvent.getId()));
    }

    //-------------Users------------------
    @Override
    public UserDto addUser(NewUserRequest newUserRequest) {
        return userMapper.toFullDto(
                userRepository.save(userMapper.toEntity(newUserRequest)));
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getAllUsers(List<Long> ids, int from, int size) {
        List<User> users;
        if (ids == null || ids.isEmpty()) {
            users = userRepository.findAll();
        } else {
            users = userRepository.findAllById(ids);
        }
        PagedListHolder page = new PagedListHolder(users
                .stream()
                .map(userMapper::toFullDto)
                .collect(Collectors.toList()));
        page.setPageSize(size);
        page.setPage(from);
        return new ArrayList<>(page.getPageList());
    }

}
