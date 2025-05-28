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
@Table(name = "Voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVoucher;

    @Column(name = "ten_voucher", nullable = false, unique = true, length = 100)
    private String tenVoucher;

    @Column(name = "phan_tram_giam")
    private Double phanTramGiam;

    @Column(name = "muc_giam_toi_da")
    private Double mucGiamToiDa;

    @Column(name = "so_tien_giam")
    private Double soTienGiam;

    @Column(name = "ngay_bat_dau", nullable = false)
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc", nullable = false)
    private LocalDate ngayKetThuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private Enum.TrangThaiVoucher trangThai = Enum.TrangThaiVoucher.hoat_dong;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @Column(name = "dieu_kien_giam")
    private String dieuKienGiam;

    @Column(name = "ngay_tao", nullable = false)
    private LocalDate ngayTao;
}
