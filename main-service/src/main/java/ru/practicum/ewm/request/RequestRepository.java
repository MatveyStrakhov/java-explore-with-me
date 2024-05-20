package ru.practicum.ewm.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.request.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("select r from Request r join r.requester u where u.id = ?1")
    List<Request> findByUserId(long userId);

    @Query("select r from Request r join r.event e where e.id = ?1")
    List<Request> findByEventId(long eventId);
}
