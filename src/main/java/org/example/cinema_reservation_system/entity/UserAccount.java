package org.example.cinema_reservation_system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.example.cinema_reservation_system.utils.Enum;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tai_khoan_nguoi_dung")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUserAccount;

    @Column(name = "ten_dang_nhap", nullable = false, unique = true, length = 100)
    private String tenDangNhap;

    @Column(name = "mat_khau", nullable = false, length = 255)
    private String matKhau;

    @Column(name = "lan_dang_nhap_cuoi")
    private LocalDateTime lanDangNhapCuoi;

    @ManyToOne
    @JoinColumn(name = "id_vai_tro", nullable = false)
    private Role vaiTro;

    @ManyToOne
    @JoinColumn(name = "id_khach_hang")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "id_nhan_vien")
    private NhanVien nhanVien;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private Enum.TrangThaiUserAccount trangThai = Enum.TrangThaiUserAccount.hoat_dong;
}
