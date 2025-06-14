package org.example.cinema_reservation_system.repository.rapchieurepository;

import org.example.cinema_reservation_system.entity.RapChieu;
import org.example.cinema_reservation_system.utils.Enum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RapChieuRepository extends JpaRepository<RapChieu, Integer> {
    List<RapChieu> findByTenRapChieuContainingIgnoreCase(String keyword);
    List<RapChieu> findByTrangThaiRapChieu(Enum.TrangThaiRapChieu trangThai);
    boolean existsByTenRapChieuIgnoreCase(String tenRapChieu);
    Optional<RapChieu> findTopByTenRapChieuOrderByIdRapChieuDesc(String ten);

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

