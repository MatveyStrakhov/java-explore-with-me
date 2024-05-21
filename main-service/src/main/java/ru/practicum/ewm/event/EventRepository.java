package ru.practicum.ewm.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventFullDto;
import ru.practicum.ewm.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select e from Event e join e.initiator u where u.id = ?1")
    List<Event> findByOwner(long userId, PageRequest pageRequest);

    @Query("select e from Event e join e.initiator u where u.id = ?1 and e.id =?2")
    Event findByOwnerAndId(long userId, long eventId);

    @Query("select e from Event e join e.initiator u join e.category c where u.id in ?1 and c.id in ?2 and e.state in ?3 and e.eventDate > ?4 and e.eventDate < ?5")
    List<Event> findEventsByAdmin(List<Long> users, List<Long> categories, List<EventState> states, LocalDateTime start, LocalDateTime end, PageRequest pageRequest);

    @Query("select e from Event e join e.category c where c.id in :catIds and e.state like 'PUBLISHED' and e.eventDate > :start and e.eventDate < :end and lower(e.annotation) || lower(e.description) || lower(e.title) like %:text% ")
    List<Event> findEventsByText(@Param(value = "text") String text, @Param(value = "catIds") List<Long> categories, @Param(value = "start") LocalDateTime start, @Param(value = "end") LocalDateTime end, PageRequest pageRequest);

    @Query("select e from Event e join e.category c where c.id in (?1) and e.state = ru.practicum.ewm.event.model.EventState.PUBLISHED and e.eventDate > ?2 and e.eventDate < ?3")
    List<Event> findEventsWithoutText(List<Long> categories, LocalDateTime start, LocalDateTime end, PageRequest pageRequest);

    @Query("select e from Event e where e.state = ru.practicum.ewm.event.model.EventState.PUBLISHED and e.eventDate > :start and e.eventDate < :end and lower(e.annotation) || lower(e.description) || lower(e.title) like %:text% ")
    List<Event> findEventsByTextWithoutCategories(@Param(value = "text") String text, @Param(value = "start") LocalDateTime start, @Param(value = "end") LocalDateTime end, PageRequest pageRequest);

    @Query("select e from Event e where e.state = ru.practicum.ewm.event.model.EventState.PUBLISHED and e.eventDate > ?1 and e.eventDate < ?2")
    List<Event> findEventsWithoutTextWithoutCategories(LocalDateTime start, LocalDateTime end, PageRequest pageRequest);

    @Query("select e from Event e join e.category c where c.id = ?1")
    List<Event> findByCategoryId(long catId);

    @Query("select e from Event e join e.location l where distance(?1, ?2, l.lat, l.lon) <= ?3")
    List<Event> findByPoint(float lat, float lon, int radius, PageRequest pageRequest);
}
