package ru.practicum.ewm.point;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.point.model.Point;

public interface PointRepository extends JpaRepository<Point, Long> {
}
