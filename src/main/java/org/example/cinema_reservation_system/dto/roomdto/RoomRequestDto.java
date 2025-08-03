package org.example.cinema_reservation_system.dto.roomdto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cinema_reservation_system.utils.enums.TrangThaiPhongChieu;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RoomRequestDto { // Add/update
    private Integer idPhongChieu; // Optional nếu dùng cho update

    @NotBlank(message = "Tên phòng chiếu không được để trống")
    @Size(max = 100, message = "Tên phòng chiếu không được vượt quá 100 ký tự")
    private String tenPhongChieu;

    @NotNull(message = "Diện tích phòng không được để trống")
    @DecimalMin(value = "100.0", message = "Diện tích phải lớn hơn hoặc bằng 100")
    private Double dienTichPhong;

    @NotNull(message = "Trạng thái không được để trống")
    private TrangThaiPhongChieu trangThaiPhongChieu;

    @NotNull(message = "Vui lòng chọn rạp chiếu")
    @Min(value = 1, message = "ID rạp chiếu không hợp lệ")
    private Integer idRapChieu;
}
