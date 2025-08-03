package org.example.cinema_reservation_system.repository.invoice;

import org.example.cinema_reservation_system.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    @Query("SELECT SUM(h.tongTien) FROM Invoice h WHERE h.trangThai = 'DA_THANH_TOAN'")
    Double getTongDoanhThu();


    @Query(value = "SELECT COALESCE(SUM(tong_tien), 0) FROM hoa_don " +
            "WHERE trang_thai = 'DA_THANH_TOAN' " +
            "AND EXTRACT(YEAR FROM ngay_dat) = :nam " +
            "AND EXTRACT(MONTH FROM ngay_dat) = :thang", nativeQuery = true)
    BigDecimal tongDoanhThuTheoThangNam(
            @Param("nam") int nam,
            @Param("thang") int thang
    );

    @Query(value = """
    SELECT r.ten_rap_chieu,
           COALESCE(SUM(h.tong_tien), 0) AS tong_doanh_thu,
           COUNT(v.id_ve_phim) AS so_ve_ban
    FROM hoa_don h
    JOIN ve_phim v ON h.id_hoa_don = v.id_hoa_don
    JOIN phong_chieu p ON v.id_phong_chieu = p.id_phong_chieu
    JOIN rap_chieu r ON p.id_rap_chieu = r.id_rap_chieu
    WHERE h.trang_thai = 'DA_THANH_TOAN'
    GROUP BY r.ten_rap_chieu
    ORDER BY tong_doanh_thu DESC
    """, nativeQuery = true)
    List<Object[]> getDoanhThuTheoRap();

    @Query(value = """
    SELECT
        p.ten_phim,
        COALESCE(SUM(h.tong_tien), 0) AS tong_doanh_thu,
        COUNT(v.id_ve_phim) AS so_ve_ban
    FROM
        hoa_don h
        JOIN ve_phim v ON h.id_hoa_don = v.id_hoa_don
        JOIN phim p ON v.id_phim = p.id_phim
    WHERE
        h.trang_thai = 'DA_THANH_TOAN'
    GROUP BY
        p.ten_phim
    ORDER BY
        tong_doanh_thu DESC
    """, nativeQuery = true)
    List<Object[]> getDoanhThuTheoPhim();



}
