package org.example.cinema_reservation_system.dto.showtimedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cinema_reservation_system.utils.enums.TrangThaiSuatChieu;

import java.time.LocalDate;
import java.time.LocalTime;

// DTO đơn giản cho danh sách hoặc dropdown

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowTimeSummaryDTO {
    private Integer idSuatChieu;
    private String tenSuatChieu;
    private LocalDate ngayChieu;
    private LocalTime gioChieu;
    private TrangThaiSuatChieu trangThai;
    private String tenPhim;
    private String tenPhongChieu;
}

