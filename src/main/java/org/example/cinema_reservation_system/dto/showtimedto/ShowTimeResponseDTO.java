package org.example.cinema_reservation_system.dto.showtimedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cinema_reservation_system.utils.enums.TrangThaiSuatChieu;

import java.time.LocalDate;
import java.time.LocalTime;

// DTO cho response với thông tin đầy đủ

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowTimeResponseDTO {
    private Integer idSuatChieu;
    private String tenSuatChieu;
    private LocalDate ngayChieu;
    private LocalTime gioChieu;
    private LocalTime thoiGianBatDau;
    private LocalTime thoiGianKetThuc;
    private TrangThaiSuatChieu trangThai;

    // Thông tin phim
    private Integer idPhim;
    private String tenPhim;
    private Integer thoiLuongPhim;

    // Thông tin phòng chiếu
    private Integer idPhongChieu;
    private String tenPhongChieu;
    private Integer soGhe;
}
