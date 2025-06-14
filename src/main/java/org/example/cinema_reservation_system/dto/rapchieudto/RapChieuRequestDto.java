package org.example.cinema_reservation_system.dto.rapchieudto;

import org.example.cinema_reservation_system.utils.Enum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RapChieuRequestDto {
    @NotBlank(message = "Tên rạp chiếu không được để trống")
    @Size(max = 100, message = "Tên rạp chiếu không quá 100 ký tự")
    private String tenRapChieu;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255)
    private String diaChi;

    @NotNull(message = "Trạng thái không được để trống")
    private Enum.TrangThaiRapChieu trangThaiRapChieu;

    @Pattern(regexp = "^0[0-9]{9}$", message = "Số điện thoại không hợp lệ")
    private String soDienThoai;
}
