package org.example.cinema_reservation_system.repository.phongchieurepository;

import org.example.cinema_reservation_system.entity.PhongChieu;
import org.example.cinema_reservation_system.utils.Enum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhongChieuRepo extends JpaRepository<PhongChieu, Integer> {
    // Tìm theo ID rạp chiếu
    java.util.List<PhongChieu> findByRapChieu_IdRapChieu(Integer idRapChieu);

    // Tìm theo trạng thái
    java.util.List<PhongChieu> findByTrangThaiPhongChieu(Enum.TrangThaiPhongChieu trangThai);

    // Tắt trạng thái phòng chiếu (đổi về KHONG_HOAT_DONG)
    @Modifying
    @Query(value = "UPDATE phong_chieu SET trang_thai = CAST(:trangThai AS enum_trang_thai_phong_chieu) WHERE id_phong_chieu = :id", nativeQuery = true)
    void changeTrangThai(@Param("id") Integer id, @Param("trangThai") String trangThai);

    //them
    @Modifying
    @Query(value = """
                INSERT INTO phong_chieu (ten_phong_chieu, dien_tich_phong, trang_thai, id_rap_chieu)
                VALUES (:ten, :dienTich, CAST(:trangThai AS enum_trang_thai_phong_chieu), :idRap)
            """, nativeQuery = true)
    void insertPhongChieu(
            @Param("ten") String ten,
            @Param("dienTich") Double dienTich,
            @Param("trangThai") String trangThai,
            @Param("idRap") Integer idRap
    );

    Optional<PhongChieu> findTopByTenPhongChieuAndRapChieu_IdRapChieuOrderByIdPhongChieuDesc(String tenPhong, Integer idRap);

    //update
    @Modifying
    @Query(value = """
                UPDATE phong_chieu
                SET ten_phong_chieu = :ten,
                    dien_tich_phong = :dienTich,
                    trang_thai = CAST(:trangThai AS enum_trang_thai_phong_chieu),
                    id_rap_chieu = :idRap
                WHERE id_phong_chieu = :id
            """, nativeQuery = true)
    void updatePhongChieu(
            @Param("id") Integer id,
            @Param("ten") String ten,
            @Param("dienTich") Double dienTich,
            @Param("trangThai") String trangThai,
            @Param("idRap") Integer idRap
    );

}
