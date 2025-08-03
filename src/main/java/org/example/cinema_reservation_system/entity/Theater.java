package org.example.cinema_reservation_system.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.example.cinema_reservation_system.utils.enums.TrangThaiRapChieu;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rapChieu")
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rap_chieu")
    private Integer idRapChieu;

    @Column(name = "ten_rap_chieu", nullable = false, unique = true, length = 100)
    private String tenRapChieu;

    @Column(name = "dia_chi", nullable = false, length = 255)
    private String diaChi;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private TrangThaiRapChieu trangThai = TrangThaiRapChieu.HOAT_DONG;

    @Column(name = "so_dien_thoai", nullable = false, length = 10)
    private String soDienThoai;
}
