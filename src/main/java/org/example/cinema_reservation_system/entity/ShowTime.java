package org.example.cinema_reservation_system.entity;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.example.cinema_reservation_system.utils.enums.TrangThaiSuatChieu;
import org.hibernate.validator.constraints.ScriptAssert;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Suat_Chieu")
@ScriptAssert(
        lang = "javascript",
        script = "_this.thoiGianBatDau != null && _this.thoiGianKetThuc != null && _this.thoiGianBatDau.isBefore(_this.thoiGianKetThuc)",
        message = "thoiGianBatDau must be before thoiGianKetThuc"
)
public class ShowTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSuatChieu;

    @NotBlank(message = "tenSuatChieu cannot be null or empty")
    @Size(max = 100, message = "tenSuatChieu must not exceed 100 characters")
    @Column(name = "ten_suat_chieu", nullable = false, length = 100)
    private String tenSuatChieu;

    @NotNull(message = "ngayChieu cannot be null")
    @FutureOrPresent(message = "ngayChieu must be today or in the future")
    @Column(name = "ngay_chieu", nullable = false)
    private LocalDate ngayChieu;

    @NotNull(message = "gioChieu cannot be null")
    @Column(name = "gio_chieu", nullable = false)
    private LocalTime gioChieu;

    @NotNull(message = "thoiGianBatDau cannot be null")
    @Column(name = "thoi_gian_bat_dau", nullable = false)
    private LocalTime thoiGianBatDau;

    @NotNull(message = "thoiGianKetThuc cannot be null")
    @Column(name = "thoi_gian_ket_thuc", nullable = false)
    private LocalTime thoiGianKetThuc;

    @NotNull(message = "trangThai cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private TrangThaiSuatChieu trangThai = TrangThaiSuatChieu.HOAN_THANH;

    @NotNull(message = "phim cannot be null")
    @ManyToOne
    @JoinColumn(name = "id_phim", nullable = false)
    private Movie phim;

    @NotNull(message = "phongChieu cannot be null")
    @ManyToOne
    @JoinColumn(name = "id_phong_chieu", nullable = false)
    private Room phongChieu;
}
