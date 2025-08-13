package org.example.cinema_reservation_system.dto.showtimedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowTimeResDTO {
    private boolean success;
    private String message;
    private DataContent data;

    @Data
    public static class DataContent {
        private MovieInfo movie;
        private CinemaInfo cinema;
        private List<AvailableDate> availableDates;
        private List<ShowtimeInfo> showtimes;
    }

    @Data
    public static class MovieInfo {
        private Long id;
        private String name;
        private String posterUrl;
    }

    @Data
    public static class CinemaInfo {
        private Long id;
        private String name;
    }

    @Data
    public static class AvailableDate {
        private String value;
        private int day;
        private String month;
        private String weekday;
    }

    @Data
    public static class ShowtimeInfo {
        private Long id;
        private String date;
        private String time;
        private String endTime;
        private int availableSeats;
        private int totalSeats;
        private boolean soldOut;
        private String format;
        // Bổ sung thêm các field lấy từ query
        private Long movieId;
        private String movieName;
        private String posterUrl;
        private Long cinemaId;
        private String cinemaName;
    }
}
