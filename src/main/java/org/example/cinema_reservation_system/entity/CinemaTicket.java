package org.example.cinema_reservation_system.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.example.cinema_reservation_system.utils.enums.TrangThaiVePhim;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ve_phim")
public class CinemaTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVePhim;

    @Column(name = "gia_ve", nullable = false)
    private BigDecimal giaVe;

    @Column(name = "ngay_dat", nullable = false)
    private LocalDate ngayDat;

    @ManyToOne
    @JoinColumn(name = "id_suat_chieu", nullable = false)
    private ShowTime suatChieu;

    @ManyToOne
    @JoinColumn(name = "id_khach_hang", nullable = false)
    private Customer khachHang;

    @ManyToOne
    @JoinColumn(name = "id_ghe_ngoi", nullable = false)
    private Seat gheNgoi;

    @ManyToOne
    @JoinColumn(name = "id_hoa_don", nullable = false)
    private Invoice hoaDon;

    @ManyToOne
    @JoinColumn(name = "id_phim", nullable = false)
    private Movie phim;

    @ManyToOne
    @JoinColumn(name = "id_nhan_vien", nullable = false)
    private Staff nhanVien;

    @ManyToOne
    @JoinColumn(name = "id_phong_chieu", nullable = false)
    private Room phongChieu;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private TrangThaiVePhim trangThai = TrangThaiVePhim.CON_TRONG;
}
