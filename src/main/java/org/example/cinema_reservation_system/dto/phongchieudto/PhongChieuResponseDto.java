package org.example.cinema_reservation_system.dto.phongchieudto;

import org.example.cinema_reservation_system.utils.Enum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhongChieuResponseDto {//trả dữ liệu về FE
    private Integer idPhongChieu;
    private String tenPhongChieu;
    private BigDecimal dienTichPhong;
    private Enum.TrangThaiPhongChieu trangThaiPhongChieu;

    // Thông tin từ Rạp Chiếu
    private Integer idRapChieu;
    private String tenRapChieu;
    private String diaChi;
    private String soDienThoai;
    private Enum.TrangThaiRapChieu trangThaiRapChieu;
}
