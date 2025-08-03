package org.example.cinema_reservation_system.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.example.cinema_reservation_system.utils.enums.TrangThaiPhongChieu;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phongChieu")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phong_chieu")
    private Integer idPhongChieu;

    @Column(name = "ten_phong_chieu", nullable = false, length = 100)
    private String tenPhongChieu;

    @Column(name = "dien_tich_phong", nullable = false)
    private BigDecimal dienTichPhong;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private TrangThaiPhongChieu trangThai = TrangThaiPhongChieu.HOAT_DONG;

    @ManyToOne
    @JoinColumn(name = "id_rap_chieu", nullable = false)
    private Theater rapChieu;
}
