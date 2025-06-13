package org.example.cinema_reservation_system.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.example.cinema_reservation_system.utils.Enum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phong_chieu")
public class PhongChieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPhongChieu;

    @Column(name = "ten_phong_chieu", nullable = false, length = 100)
    private String tenPhongChieu;

    @Column(name = "dien_tich_phong", nullable = false)
    private Double dienTichPhong;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, columnDefinition = "enum_trang_thai_phong_chieu")
    private Enum.TrangThaiPhongChieu trangThaiPhongChieu = Enum.TrangThaiPhongChieu.HOAT_DONG;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rap_chieu", nullable = false)
    private RapChieu rapChieu;

    @Override
    public String toString() {
        return "PhongChieu{" +
                "idPhongChieu=" + idPhongChieu +
                ", tenPhongChieu='" + tenPhongChieu + '\'' +
                ", dienTichPhong=" + dienTichPhong +
                ", trangThai=" + trangThaiPhongChieu +
                ", rapChieu=" + rapChieu +
                '}';
    }
}
