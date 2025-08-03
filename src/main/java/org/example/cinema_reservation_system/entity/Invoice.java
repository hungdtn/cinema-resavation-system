package org.example.cinema_reservation_system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.example.cinema_reservation_system.utils.Enum;
import org.example.cinema_reservation_system.utils.enums.TrangThaiHoaDon;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hoa_don")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hoa_don")
    private Integer idHoaDon;

    @Column(name = "ten_hoa_don", nullable = false, length = 100)
    private String tenHoaDon;

    @Column(name = "tong_tien", nullable = false)
    private BigDecimal tongTien;

    @Column(name = "tien_giam")
    private BigDecimal tienGiam;

    @Column(name = "ngay_dat", nullable = false)
    private LocalDate ngayDat;

    @Column(name = "loai_hoa_don", nullable = false, length = 50)
    private String loaiHoaDon;

    @Column(name = "so_dien_thoai", nullable = false, length = 15)
    private String soDienThoai;

    @Column(name = "ten_khach_hang", nullable = false, length = 100)
    private String tenKhachHang;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private TrangThaiHoaDon trangThai = TrangThaiHoaDon.DA_THANH_TOAN;

    @ManyToOne
    @JoinColumn(name = "id_khach_hang")
    private Customer khachHang;

    @ManyToOne
    @JoinColumn(name = "id_nhan_vien")
    private Staff nhanVien;

    @ManyToOne
    @JoinColumn(name = "id_voucher")
    private Voucher voucher;

}
