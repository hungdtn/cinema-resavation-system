package org.example.cinema_reservation_system.dto.suatchieudto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cinema_reservation_system.utils.Enum;

import java.time.LocalDate;
import java.time.LocalTime;

// DTO đơn giản cho danh sách hoặc dropdown

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuatChieuSummaryDTO {
    private Integer idSuatChieu;
    private String tenSuatChieu;
    private LocalDate ngayChieu;
    private LocalTime gioChieu;
    private Enum.TrangThaiSuatChieu trangThai;
    private String tenPhim;
    private String tenPhongChieu;
}

