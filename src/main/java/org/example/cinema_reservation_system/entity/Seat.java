package org.example.cinema_reservation_system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.example.cinema_reservation_system.utils.enums.TrangThaiGheNgoi;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Ghe_Ngoi", uniqueConstraints = @UniqueConstraint(columnNames = {"id_phong_chieu", "hang_ghe", "so_ghe"}))
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ghe_ngoi")
    private Integer idGheNgoi;

    @NotBlank(message = "Hàng ghế không được để trống")
    @Size(min = 1, max = 10, message = "Hàng ghế phải từ 1-10 ký tự")
    @Pattern(regexp = "^[A-Z]+$", message = "Hàng ghế chỉ được chứa chữ cái viết hoa")
    @Column(name = "hang_ghe", nullable = false, length = 10)
    private String hangGhe;

    @NotBlank(message = "Số ghế không được để trống")
    @Size(min = 1, max = 10, message = "Số ghế phải từ 1-10 ký tự")
    @Pattern(regexp = "^[0-9]+$", message = "Số ghế chỉ được chứa số")
    @Column(name = "so_ghe", nullable = false, length = 10)
    private String soGhe;

    @NotNull(message = "Phòng chiếu không được null")
    @ManyToOne
    @JoinColumn(name = "id_phong_chieu", nullable = false)
    private Room phongChieu;

    @NotNull(message = "Trạng thái ghế không được null")
    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private TrangThaiGheNgoi trangThai;

    @NotBlank(message = "Loại ghế không được để trống")
    @Size(min = 1, max = 50, message = "Loại ghế phải từ 1-50 ký tự")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s_-]+$", message = "Loại ghế chỉ được chứa chữ cái, số, khoảng trắng, gạch dưới và gạch ngang")
    @Column(name = "loai_ghe", nullable = false, length = 50)
    private String loaiGhe;

    // Custom validation method - có thể sử dụng với @Valid
    @AssertTrue(message = "Hàng ghế và số ghế phải tạo thành một vị trí ghế hợp lệ")
    public boolean isViTriGheHopLe() {
        if (hangGhe == null || soGhe == null) {
            return false;
        }

        // Kiểm tra hàng ghế từ A-Z
        if (!hangGhe.matches("^[A-Z]+$")) {
            return false;
        }

        // Kiểm tra số ghế từ 1-999
        try {
            int soGheInt = Integer.parseInt(soGhe);
            return soGheInt >= 1 && soGheInt <= 999;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}