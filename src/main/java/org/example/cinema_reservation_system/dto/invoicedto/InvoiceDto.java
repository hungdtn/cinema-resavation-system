package org.example.cinema_reservation_system.dto.invoicedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cinema_reservation_system.utils.enums.TrangThaiHoaDon;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceDto {
    private Integer idHoaDon;
    private String tenHoaDon;
    private Double tongTien;
    private Double tienGiam;
    private LocalDate ngayDat;
    private String loaiHoaDon;
    private TrangThaiHoaDon trangThai;

    // Thông tin từ bảng KhachHang (liên kết ManyToOne)
    private String tenKhachHang;
    private String soDienThoaiKhachHang;

    // Thông tin từ bảng NhanVien
    private String tenNhanVien;

    // Thông tin từ bảng Voucher
    private String tenVoucher;
    private Double soTienGiam;
}
