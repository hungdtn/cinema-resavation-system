package org.example.cinema_reservation_system.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.example.cinema_reservation_system.utils.enums.TrangThaiVePhim;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vephim")
public class VePhim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ve_phim")
    private Integer idVePhim;

    @Column(name = "gia_ve")
    private Double giaVe;

    @Column(name = "ngay_dat")
    private LocalDate ngayDat;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_suat_chieu")
    private SuatChieu suatChieu;

    @ManyToOne
    @JoinColumn(name = "id_khach_hang")
    private KhachHang khachHang;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_ghe_ngoi")
    private GheNgoi gheNgoi;

    @ManyToOne
    @JoinColumn(name = "id_hoa_don")
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "id_phim")
    private Phim phim;

    @ManyToOne
    @JoinColumn(name = "id_nhan_vien")
    private NhanVien nhanVien;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", columnDefinition = "enum_trang_thai_ve_phim", nullable = false)
    private TrangThaiVePhim trangThai;

}
