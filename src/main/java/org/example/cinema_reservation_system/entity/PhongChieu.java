package org.example.cinema_reservation_system.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.example.cinema_reservation_system.utils.Enum;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Phong_Chieu")
public class PhongChieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phong_chieu")
    private Integer id;

    @Column(name = "ten_phong_chieu", nullable = false, length = 100)
    private String tenPhongChieu;

    @Column(name = "dien_tich_phong", nullable = false, precision = 10, scale = 2)
    private BigDecimal dienTichPhong;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private Enum.TrangThaiPhongChieu trangThai = Enum.TrangThaiPhongChieu.hoat_dong;

    @ManyToOne
    @JoinColumn(name = "id_rap_chieu", nullable = false)
    private RapChieu rapChieu;
}
