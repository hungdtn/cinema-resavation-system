package org.example.cinema_reservation_system.repository.theater;

import org.example.cinema_reservation_system.entity.Theater;
import org.example.cinema_reservation_system.utils.enums.TrangThaiRapChieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Integer> {
    List<Theater> findByTenRapChieuContainingIgnoreCase(String keyword);
    List<Theater> findByTrangThai(TrangThaiRapChieu trangThai);
    boolean existsByTenRapChieuIgnoreCase(String tenRapChieu);

    Optional<Theater> findTopByTenRapChieuOrderByIdRapChieuDesc(String ten); // ✅ Sửa ở đây

    @Modifying
    @Query(value = """
        INSERT INTO rap_chieu (ten_rap_chieu, dia_chi, so_dien_thoai, trang_thai)
        VALUES (:ten, :diaChi, :soDienThoai, CAST(:trangThai AS enum_trang_thai_rap_chieu))
    """, nativeQuery = true)
    void insertRapChieu(
            @Param("ten") String ten,
            @Param("diaChi") String diaChi,
            @Param("soDienThoai") String soDienThoai,
            @Param("trangThai") String trangThai
    );

    @Modifying(clearAutomatically = true)
    @Query(value = """
        UPDATE rap_chieu
        SET ten_rap_chieu = :ten,
            dia_chi = :diaChi,
            so_dien_thoai = :soDienThoai,
            trang_thai = CAST(:trangThai AS enum_trang_thai_rap_chieu)
        WHERE id_rap_chieu = :id
    """, nativeQuery = true)
    void updateRapChieu(
            @Param("id") Integer id,
            @Param("ten") String ten,
            @Param("diaChi") String diaChi,
            @Param("soDienThoai") String soDienThoai,
            @Param("trangThai") String trangThai
    );
}


