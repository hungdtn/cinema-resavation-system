package org.example.cinema_reservation_system.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.example.cinema_reservation_system.Utils.Enum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GheNgoi", uniqueConstraints = @UniqueConstraint(columnNames = {"id_phong_chieu", "hang_ghe", "so_ghe"}))
public class GheNgoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idGheNgoi;

    @Column(name = "hang_ghe", nullable = false, length = 10)
    private String hangGhe;

    @Column(name = "so_ghe", nullable = false, length = 10)
    private String soGhe;

    @ManyToOne
    @JoinColumn(name = "id_phong_chieu", nullable = false)
    private PhongChieu phongChieu;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private Enum.TrangThaiGheNgoi trangThai = Enum.TrangThaiGheNgoi.con_trong;

    @Column(name = "loai_ghe", nullable = false, length = 50)
    private String loaiGhe;
}