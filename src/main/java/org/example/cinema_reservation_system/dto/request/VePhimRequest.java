package org.example.cinema_reservation_system.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VePhimRequest {
    private Double giaVe;
    private LocalDate ngayDat;
    private Integer idSuatChieu;
    private Integer idKhachHang;
    private Integer idGheNgoi;
    private Integer idHoaDon;
    private Integer idPhim;
    private Integer idNhanVien;
    private String trangThai; // String, sẽ convert sang enum
}
