package org.example.cinema_reservation_system.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.example.cinema_reservation_system.utils.enums.TrangThaiKhachHang;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "khach_hang")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_khach_hang")
    private Integer idKhachHang;

    @Column(name = "ten_khach_hang", nullable = false, length = 100)
    private String tenKhachHang;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "so_dien_thoai", nullable = false, length = 15)
    private String soDienThoaiKhachHang;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "gioi_tinh", length = 10)
    private String gioiTinh;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiKhachHang trangThai = TrangThaiKhachHang.HOAT_DONG;

    @Column(name = "diem_tich_luy")
    private Integer diemTichLuy = 0;

}
