package org.example.cinema_reservation_system.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.example.cinema_reservation_system.Utils.Enum;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SuatChieu")
public class SuatChieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSuatChieu;

    @Column(name = "ten_suat_chieu", nullable = false, length = 100)
    private String tenSuatChieu;

    @Column(name = "ngay_chieu", nullable = false)
    private LocalDate ngayChieu;

    @Column(name = "gio_chieu", nullable = false)
    private LocalTime gioChieu;

    @Column(name = "thoi_gian_bat_dau", nullable = false)
    private LocalTime thoiGianBatDau;

    @Column(name = "thoi_gian_ket_thuc", nullable = false)
    private LocalTime thoiGianKetThuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private Enum.TrangThaiSuatChieu trangThai = Enum.TrangThaiSuatChieu.da_len_lich;

    @ManyToOne
    @JoinColumn(name = "id_phim", nullable = false)
    private Phim phim;

    @ManyToOne
    @JoinColumn(name = "id_phong_chieu", nullable = false)
    private PhongChieu phongChieu;
}
