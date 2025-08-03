package org.example.cinema_reservation_system.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.cinema_reservation_system.repository.cinematicket.CinemaTicketRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository

public class CinemaTicketRepositoryImpl implements CinemaTicketRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void insertVePhimNative(
            Integer idGheNgoi,
            BigDecimal giaVe,
            Integer idHoaDon,
            Integer idKhachHang,
            java.sql.Date ngayDat,
            Integer idNhanVien,
            Integer idPhim,
            Integer idSuatChieu,
            Integer idPhongChieu,
            String trangThai
    ) {
        entityManager.createNativeQuery("""
            INSERT INTO ve_phim (
                id_ghe_ngoi, gia_ve, id_hoa_don, id_khach_hang,
                ngay_dat, id_nhan_vien, id_phim, id_suat_chieu,id_phong_chieu, trang_thai
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? , ?::enum_trang_thai_ve_phim)
        """)
                .setParameter(1, idGheNgoi)
                .setParameter(2, giaVe)
                .setParameter(3, idHoaDon)
                .setParameter(4, idKhachHang)
                .setParameter(5, ngayDat)
                .setParameter(6, idNhanVien)
                .setParameter(7, idPhim)
                .setParameter(8, idSuatChieu)
                .setParameter(9, idPhongChieu)
                .setParameter(10, trangThai)
                .executeUpdate();
    }
}
