package org.example.cinema_reservation_system.dto.suatchieudto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cinema_reservation_system.utils.Enum;

// DTO cho việc cập nhật trạng thái
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuatChieuStatusUpdateDTO {
    @NotNull(message = "idSuatChieu cannot be null")
    private Integer idSuatChieu;

    @NotNull(message = "trangThai cannot be null")
    private Enum.TrangThaiSuatChieu trangThai;
}
