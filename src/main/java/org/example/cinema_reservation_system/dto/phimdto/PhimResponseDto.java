package org.example.cinema_reservation_system.dto.phimdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cinema_reservation_system.utils.Enum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhimResponseDto {
    private Integer idPhim;

    private String tenPhim;

    private String moTa;

    private Integer thoiLuong;

    private LocalDate ngayPhatHanh;

    private Enum.TrangThaiPhim trangThai;

    private String dinhDang;

    private LocalDate ngayTao;

    private String posterUrl;

    private String bannerUrl;

    private String trailerUrl;


    private List<String> daoDien;
    private List<String> dienVien;
    private List<String> theLoai;

}
