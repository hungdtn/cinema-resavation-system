package org.example.cinema_reservation_system.dto.seatdto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.*;
import org.example.cinema_reservation_system.utils.enums.TrangThaiGheNgoi;

// DTO chính - dùng cho tất cả operations
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeatDTO {

    private Integer idGheNgoi; // null khi create, có giá trị khi update/response

    @NotBlank(message = "Hàng ghế không được để trống")
    @Pattern(regexp = "^[A-Z]+$", message = "Hàng ghế chỉ được chứa chữ cái viết hoa")
    private String hangGhe;

    @NotBlank(message = "Số ghế không được để trống")
    @Pattern(regexp = "^[0-9]+$", message = "Số ghế chỉ được chứa số")
    private String soGhe;

    @NotNull(message = "ID phòng chiếu không được null")
    private Integer idPhongChieu;

    // Thông tin bổ sung - chỉ có khi response
    private String tenPhongChieu;
    private String viTriGhe; // "A1", "B5"

    @NotNull(message = "Trạng thái ghế không được null")
    @Enumerated(EnumType.STRING)
    private TrangThaiGheNgoi trangThai;

    @NotBlank(message = "Loại ghế không được để trống")
    private String loaiGhe;

    // Computed properties
    public String getViTriGhe() {
        if (hangGhe != null && soGhe != null) {
            return hangGhe + soGhe;
        }
        return viTriGhe;
    }
}