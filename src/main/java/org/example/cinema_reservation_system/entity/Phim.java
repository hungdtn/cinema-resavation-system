package org.example.cinema_reservation_system.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.example.cinema_reservation_system.utils.Enum;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Phim")
public class Phim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPhim;

    @Column(name = "ten_phim", nullable = false, unique = true, length = 100)
    private String tenPhim;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "thoi_luong", nullable = false)
    private Integer thoiLuong;

    @Column(name = "ngay_phat_hanh", nullable = false)
    private LocalDate ngayPhatHanh;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private Enum.TrangThaiPhim trangThai = Enum.TrangThaiPhim.hoat_dong;

    @Column(name = "dinh_dang", nullable = false, length = 50)
    private String dinhDang;

    @Column(name = "ngay_tao", nullable = false)
    private LocalDate ngayTao;
}
