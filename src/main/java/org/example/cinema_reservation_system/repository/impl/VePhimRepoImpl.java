package org.example.cinema_reservation_system.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.cinema_reservation_system.entity.VePhim;
import org.example.cinema_reservation_system.repository.VePhimRepo;
import org.example.cinema_reservation_system.repository.VePhimRepoCustom;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository

public class VePhimRepoImpl implements VePhimRepoCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void insertVePhimNative(
            Integer idGheNgoi,
            Double giaVe,
            Integer idHoaDon,
            Integer idKhachHang,
            java.sql.Date ngayDat,
            Integer idNhanVien,
            Integer idPhim,
            Integer idSuatChieu,
            String trangThai
    ) {
        entityManager.createNativeQuery("""
            INSERT INTO vephim (
                id_ghe_ngoi, gia_ve, id_hoa_don, id_khach_hang,
                ngay_dat, id_nhan_vien, id_phim, id_suat_chieu, trang_thai
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::enum_trang_thai_ve_phim)
        """)
                .setParameter(1, idGheNgoi)
                .setParameter(2, giaVe)
                .setParameter(3, idHoaDon)
                .setParameter(4, idKhachHang)
                .setParameter(5, ngayDat)
                .setParameter(6, idNhanVien)
                .setParameter(7, idPhim)
                .setParameter(8, idSuatChieu)
                .setParameter(9, trangThai)
                .executeUpdate();
    }
}
