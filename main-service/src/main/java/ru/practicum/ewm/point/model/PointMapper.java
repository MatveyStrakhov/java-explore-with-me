package ru.practicum.ewm.point.model;

public class PointMapper {
    public static Point toPoint(PointCreateDto pointCreateDto) {
        return Point.builder()
                .lat(pointCreateDto.getLat())
                .lon(pointCreateDto.getLon())
                .name(pointCreateDto.getName())
                .radius(pointCreateDto.getRadius())
                .build();
    }

    public static PointDto toPointDto(Point point) {
        return PointDto.builder()
                .id(point.getId())
                .lat(point.getLat())
                .lon(point.getLon())
                .name(point.getName())
                .radius(point.getRadius())
                .build();
    }
}
