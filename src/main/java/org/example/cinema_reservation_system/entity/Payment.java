package org.example.cinema_reservation_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cinema_reservation_system.utils.Enum;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "thanh_toan")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thanh_toan")
    private Integer idThanhToan;

    @ManyToOne
    @JoinColumn(name = "id_hoa_don", nullable = false)
    private Invoice hoaDon;

    @Column(name = "phuong_thuc_thanh_toan", nullable = false)
    private String phuongThucThanhToan;

    @Column(name = "ngay_thanh_toan", nullable = false)
    private LocalDate ngayThanhToan;

    @Column(name = "so_tien", nullable = false)
    private BigDecimal soTien;

    @NotNull(message = "Trạng thái ghế không được null")
    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private Enum.TrangThaiThanhToan trangThai = Enum.TrangThaiThanhToan.CHO_XU_LY;

}
