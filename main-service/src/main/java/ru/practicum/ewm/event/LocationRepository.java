package ru.practicum.ewm.event;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.location.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Location getLocationByLatAndLon(float lat, float lon);
}
