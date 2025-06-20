package org.example.cinema_reservation_system.dto.rapchieudto;
import org.example.cinema_reservation_system.utils.Enum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RapChieuResponseDto {
    private Integer idRapChieu;
    private String tenRapChieu;
    private String diaChi;
    private Enum.TrangThaiRapChieu trangThaiRapChieu;
    private String soDienThoai;
}
