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
@Table(name = "ghengoi")
public class GheNgoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ghe_ngoi")
    private Integer idGheNgoi;

    @Column(name = "hang_ghe")
    private String hangGhe;

    @Column(name = "so_ghe")
    private String soGhe;

    @ManyToOne
    @JoinColumn(name = "id_phong_chieu")
    private PhongChieu phongChieu;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private Enum.TrangThaiGheNgoi trangThai = Enum.TrangThaiGheNgoi.con_trong;

    @Column(name = "loai_ghe")
    private String loaiGhe;

}