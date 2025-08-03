package org.example.cinema_reservation_system.dto.moviedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cinema_reservation_system.utils.enums.TrangThaiPhim;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponseDto {
    private Integer idPhim;

    private String tenPhim;

    private String moTa;

    private Integer thoiLuong;

    private LocalDate ngayPhatHanh;

    private TrangThaiPhim trangThai;

    private String dinhDang;

    private LocalDate ngayTao;

    private String posterUrl;

    private String bannerUrl;

    private String trailerUrl;


    private List<String> daoDien;
    private List<String> dienVien;
    private List<String> theLoai;

}
