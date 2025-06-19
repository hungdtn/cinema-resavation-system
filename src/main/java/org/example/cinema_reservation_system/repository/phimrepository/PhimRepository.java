package org.example.cinema_reservation_system.repository.phimrepository;

import org.example.cinema_reservation_system.entity.Phim;
import org.example.cinema_reservation_system.utils.Enum;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhimRepository extends JpaRepository<Phim, Integer>, JpaSpecificationExecutor<Phim> {
    // 1. Lọc theo thể loại & trạng thái
    @Query("SELECT p FROM Phim p JOIN p.theLoaiList t WHERE LOWER(t.tenTheLoai) = LOWER(:theLoai) AND p.trangThai = :trangThai")
    List<Phim> findByTheLoaiAndTrangThaiIgnoreCase(@Param("theLoai") String theLoai, @Param("trangThai") String trangThai);

    // 2. Lọc theo thể loại
    @Query("SELECT p FROM Phim p JOIN p.theLoaiList tl WHERE LOWER(tl.tenTheLoai) = LOWER(:tenTheLoai)")
    List<Phim> findByTheLoaiIgnoreCase(@Param("tenTheLoai") String tenTheLoai);

    // 3. Lọc theo trạng thái
    List<Phim> findByTrangThai(Enum.TrangThaiPhim trangThai);

    @EntityGraph(attributePaths = {"hinhAnhs"})
    @Query("SELECT p FROM Phim p")
    List<Phim> findAllFetchHinhAnh();

    @Modifying
    @Query(value = "INSERT INTO phim (ten_phim, mo_ta, thoi_luong, ngay_phat_hanh, trang_thai, dinh_dang, ngay_tao) " +
            "VALUES (:tenPhim, :moTa, :thoiLuong, :ngayPhatHanh, CAST(:trangThai AS enum_trang_thai_phim), :dinhDang, :ngayTao)",
            nativeQuery = true)
    void insertPhim(@Param("tenPhim") String tenPhim,
                    @Param("moTa") String moTa,
                    @Param("thoiLuong") Integer thoiLuong,
                    @Param("ngayPhatHanh") LocalDate ngayPhatHanh,
                    @Param("trangThai") String trangThai,
                    @Param("dinhDang") String dinhDang,
                    @Param("ngayTao") LocalDate ngayTao);

    Optional<Phim> findByTenPhim(String tenPhim);

    @Modifying
    @Query(value = "UPDATE phim SET ten_phim = :tenPhim, mo_ta = :moTa, thoi_luong = :thoiLuong, " +
            "ngay_phat_hanh = :ngayPhatHanh, trang_thai = CAST(:trangThai AS enum_trang_thai_phim), " +
            "dinh_dang = :dinhDang, ngay_tao = :ngayTao WHERE id_phim = :idPhim", nativeQuery = true)
    void updatePhim(@Param("idPhim") Integer idPhim,
                    @Param("tenPhim") String tenPhim,
                    @Param("moTa") String moTa,
                    @Param("thoiLuong") Integer thoiLuong,
                    @Param("ngayPhatHanh") LocalDate ngayPhatHanh,
                    @Param("trangThai") String trangThai,
                    @Param("dinhDang") String dinhDang,
                    @Param("ngayTao") LocalDate ngayTao);

    @Query(value = """
                SELECT * FROM phim\s
                WHERE (:theLoai IS NULL OR EXISTS (
                    SELECT 1\s
                    FROM phim_the_loai pt
                    JOIN the_loai tl ON pt.id_the_loai_phim = tl.id_the_loai 
                    WHERE pt.id_phim = phim.id_phim\s
                    AND LOWER(tl.ten_the_loai) = LOWER(:theLoai)
                ))
                AND (:trangThai IS NULL OR trang_thai = CAST(:trangThai AS enum_trang_thai_phim))
            """, nativeQuery = true)
    List<Phim> filterPhim(@Param("theLoai") String theLoai,
                          @Param("trangThai") String trangThai);

}
