package org.example.cinema_reservation_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "ThanhToan")
public class ThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thanh_toan")
    private Integer idThanhToan;

    @Column(name = "id_hoa_don", nullable = false)
    private Integer idHoaDon;

    @Column(name = "phuong_thuc_thanh_toan", nullable = false)
    private String phuongThucThanhToan;

    @Column(name = "ngay_thanh_toan", nullable = false)
    private LocalDate ngayThanhToan;

    @Column(name = "so_tien", nullable = false)
    private BigDecimal soTien;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private TrangThaiThanhToan trangThai = TrangThaiThanhToan.CHO_XU_LY;

    public enum TrangThaiThanhToan {
        CHO_XU_LY, HOAN_THANH, DA_HUY
    }

}
