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
@Table(name = "Rap_Chieu")
public class RapChieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rap_chieu")
    private Integer id;

    @Column(name = "ten_rap_chieu", nullable = false, unique = true, length = 100)
    private String tenRapChieu;

    @Column(name = "dia_chi", nullable = false, length = 255)
    private String diaChi;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private Enum.TrangThaiRapChieu trangThai = Enum.TrangThaiRapChieu.hoat_dong;

    @Column(name = "so_dien_thoai", nullable = false, length = 10)
    private String soDienThoai;
}
