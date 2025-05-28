package org.example.cinema_reservation_system.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.example.cinema_reservation_system.Utils.Enum;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "VePhim")
public class VePhim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVePhim;

    @Column(name = "gia_ve", nullable = false)
    private Double giaVe;

    @Column(name = "ngay_dat", nullable = false)
    private LocalDate ngayDat;

    @ManyToOne
    @JoinColumn(name = "id_suat_chieu", nullable = false)
    private SuatChieu suatChieu;

    @ManyToOne
    @JoinColumn(name = "id_khach_hang", nullable = false)
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "id_ghe_ngoi", nullable = false)
    private GheNgoi gheNgoi;

    @ManyToOne
    @JoinColumn(name = "id_hoa_don", nullable = false)
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "id_phim", nullable = false)
    private Phim phim;

    @ManyToOne
    @JoinColumn(name = "id_nhan_vien", nullable = false)
    private NhanVien nhanVien;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private Enum.TrangThaiVePhim trangThai = Enum.TrangThaiVePhim.con_trong;
}
