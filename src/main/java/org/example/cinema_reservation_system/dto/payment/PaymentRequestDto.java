package org.example.cinema_reservation_system.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
    private Integer idHoaDon;

    @NotBlank(message = "Phương thức thanh toán không được trống")
    private String phuongThucThanhToan;

    @NotBlank(message = "Id lịch chiếu không được trống")
    private String scheduleId;

    @NotEmpty(message = "Danh sách ghế không được trống")
    private List<String> seatIds;

    @NotBlank(message = "Tên hóa đơn không được để trống")
    private String tenHoaDon;

    @NotBlank(message = "Tên khách hàng không được để trống")
    private String tenKhachHang;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String soDienThoai;

    @NotBlank(message = "Loại hóa đơn không được để trống")
    private String loaiHoaDon;
}


