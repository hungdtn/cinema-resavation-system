package org.example.cinema_reservation_system.repository.cinematicket;

import java.math.BigDecimal;

public interface CinemaTicketRepositoryCustom {
    void insertVePhimNative(
            Integer idGheNgoi,
            BigDecimal giaVe,
            Integer idHoaDon,
            Integer idKhachHang,
            java.sql.Date ngayDat,
            Integer idNhanVien,
            Integer idPhim,
            Integer idSuatChieu,
            Integer idPhongChieu,
            String trangThai // phải truyền đúng chuỗi enum: "con_trong", "da_ban", "da_dat"
    );
}
