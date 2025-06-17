package org.example.cinema_reservation_system.dto.suatchieudto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cinema_reservation_system.utils.Enum;
import org.hibernate.validator.constraints.ScriptAssert;

import java.time.LocalDate;
import java.time.LocalTime;

//dto dùng cho việc tạo mới và update

@Data
@AllArgsConstructor
@NoArgsConstructor
@ScriptAssert(
        lang = "javascript",
        script = "_this.thoiGianBatDau != null && _this.thoiGianKetThuc != null && _this.thoiGianBatDau.isBefore(_this.thoiGianKetThuc)",
        message = "thoiGianBatDau must be before thoiGianKetThuc"
)
public class SuatChieuRequestDTO {
    @NotBlank(message = "tenSuatChieu cannot be null or empty")
    @Size(max = 100, message = "tenSuatChieu must not exceed 100 characters")
    private String tenSuatChieu;

    @NotNull(message = "ngayChieu cannot be null")
    @FutureOrPresent(message = "ngayChieu must be today or in the future")
    private LocalDate ngayChieu;

    @NotNull(message = "gioChieu cannot be null")
    private LocalTime gioChieu;

    @NotNull(message = "thoiGianBatDau cannot be null")
    private LocalTime thoiGianBatDau;

    @NotNull(message = "thoiGianKetThuc cannot be null")
    private LocalTime thoiGianKetThuc;

    private Enum.TrangThaiSuatChieu trangThai = Enum.TrangThaiSuatChieu.da_len_lich;

    @NotNull(message = "idPhim cannot be null")
    private Integer idPhim;

    @NotNull(message = "idPhongChieu cannot be null")
    private Integer idPhongChieu;
}
