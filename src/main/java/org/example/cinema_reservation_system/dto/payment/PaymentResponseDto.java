package org.example.cinema_reservation_system.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cinema_reservation_system.utils.Enum;
import org.example.cinema_reservation_system.utils.enums.TrangThaiThanhToan;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {
    private Integer idThanhToan;
    private Integer idHoaDon;
    private BigDecimal soTien;
    private String phuongThucThanhToan;
    private Enum.TrangThaiThanhToan trangThai;
    private LocalDate ngayThanhToan;
    private String paymentUrl;

    public PaymentResponseDto(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public PaymentResponseDto(Object o, String s) {
    }
}
