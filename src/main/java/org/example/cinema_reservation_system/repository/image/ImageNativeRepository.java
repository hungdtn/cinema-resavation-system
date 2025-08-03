package org.example.cinema_reservation_system.repository.image;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ImageNativeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Thêm ảnh vào bảng hinh_anh bằng native query và ép kiểu enum rõ ràng.
     *
     * @param tenHinhAnh      Tên ảnh gốc (tên file)
     * @param loaiHinhAnhEnumName Tên enum kiểu chuỗi, ví dụ: "POSTER" hoặc "BANNER"
     * @param url             Đường dẫn ảnh đã upload
     * @param idPhim          ID của phim liên kết
     */

    public void insertImage(String tenHinhAnh, String loaiHinhAnhEnumName, String url, Integer idPhim) {
        String sql = """
            INSERT INTO hinh_anh (ten_hinh_anh, loai_hinh_anh, url, id_phim)
            VALUES (?, CAST(? AS enum_loai_hinh_anh), ?, ?)
        """;

        entityManager.createNativeQuery(sql)
                .setParameter(1, tenHinhAnh)
                .setParameter(2, loaiHinhAnhEnumName) // gọi .name() từ enum
                .setParameter(3, url)
                .setParameter(4, idPhim)
                .executeUpdate();
    }
}
