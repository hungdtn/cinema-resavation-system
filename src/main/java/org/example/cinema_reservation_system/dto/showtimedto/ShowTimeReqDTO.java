package org.example.cinema_reservation_system.dto.showtimedto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowTimeReqDTO {
    @NotNull(message = "ID phim không được để trống")
    private Long movieId;

    @NotNull(message = "ID rạp không được để trống")
    private Long cinemaId;

    // Optional: nếu muốn lọc 1 ngày cụ thể
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
