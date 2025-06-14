package org.example.cinema_reservation_system.repository;

public interface VePhimRepoCustom {
    void insertVePhimNative(
            Integer idGheNgoi,
            Double giaVe,
            Integer idHoaDon,
            Integer idKhachHang,
            java.sql.Date ngayDat,
            Integer idNhanVien,
            Integer idPhim,
            Integer idSuatChieu,
            String trangThai // phải truyền đúng chuỗi enum: "con_trong", "da_ban", "da_dat"
    );
}
