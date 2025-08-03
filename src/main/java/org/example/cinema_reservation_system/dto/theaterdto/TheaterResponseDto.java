package org.example.cinema_reservation_system.dto.theaterdto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cinema_reservation_system.utils.enums.TrangThaiRapChieu;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheaterResponseDto {
    private Integer idRapChieu;
    private String tenRapChieu;
    private String diaChi;
    private TrangThaiRapChieu trangThaiRapChieu;
    private String soDienThoai;
}
