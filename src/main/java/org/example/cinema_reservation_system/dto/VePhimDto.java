package org.example.cinema_reservation_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cinema_reservation_system.utils.Enum;
import org.example.cinema_reservation_system.utils.enums.TrangThaiVePhim;

import java.time.LocalDate;
import java.time.LocalTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VePhimDto {
    private int idVePhim;
    private double giaVe;
    private LocalDate ngayDat;
    private TrangThaiVePhim trangThai;

    private String tenPhim;
    private String tenNhanVien;
    private LocalDate ngayChieu;
    private LocalTime gioChieu;
    private String soGhe;
    private String tenSuatChieu;
    private String tenKhachHang;
    private String soDienThoaiKhachHang;

}
