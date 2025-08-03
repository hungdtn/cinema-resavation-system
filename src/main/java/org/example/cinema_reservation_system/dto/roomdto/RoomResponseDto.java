package org.example.cinema_reservation_system.dto.roomdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cinema_reservation_system.utils.enums.TrangThaiPhongChieu;
import org.example.cinema_reservation_system.utils.enums.TrangThaiRapChieu;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponseDto {//trả dữ liệu về FE
    private Integer idPhongChieu;
    private String tenPhongChieu;
    private BigDecimal dienTichPhong;
    private TrangThaiPhongChieu trangThaiPhongChieu;

    // Thông tin từ Rạp Chiếu
    private Integer idRapChieu;
    private String tenRapChieu;
    private String diaChi;
    private String soDienThoai;
    private TrangThaiRapChieu trangThaiRapChieu;
}
