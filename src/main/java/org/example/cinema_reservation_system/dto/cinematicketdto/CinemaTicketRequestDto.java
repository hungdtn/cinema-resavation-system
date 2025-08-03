package org.example.cinema_reservation_system.dto.cinematicketdto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CinemaTicketRequestDto {
    @NotNull(message = "Giá vé không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá vé phải lớn hơn 0")
    private BigDecimal giaVe;

    @NotNull(message = "Ngày đặt không được để trống")
    @PastOrPresent(message = "Ngày đặt không được lớn hơn ngày hiện tại")
    private LocalDate ngayDat;

    @NotNull(message = "ID suất chiếu không được để trống")
    private Integer idSuatChieu;

    @NotNull(message = "ID khách hàng không được để trống")
    private Integer idKhachHang;

    @NotNull(message = "ID ghế ngồi không được để trống")
    private Integer idGheNgoi;

    @NotNull(message = "ID hóa đơn không được để trống")
    private Integer idHoaDon;

    @NotNull(message = "ID phim không được để trống")
    private Integer idPhim;

    @NotNull(message = "ID nhân viên không được để trống")
    private Integer idNhanVien;

    @NotNull(message = "ID phòng chiếu không được để trống")
    private Integer idPhongChieu;

    @NotBlank(message = "Trạng thái vé không được để trống")
    private String trangThai; // sẽ convert sang enum TrangThaiVePhim trong Service
}
