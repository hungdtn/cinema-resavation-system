package org.example.cinema_reservation_system.dto.phimdto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.Set;

import org.example.cinema_reservation_system.utils.Enum;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhimRequestDto {

    @NotBlank(message = "Tên phim không được để trống")
    @Size(max = 100, message = "Tên phim tối đa 100 ký tự")
    private String tenPhim;

    @NotBlank(message = "Mô tả không được để trống")
    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String moTa;

    @NotNull(message = "Thời lượng không được để trống")
    @Min(value = 1, message = "Thời lượng phải lớn hơn 0")
    private Integer thoiLuong;

    @NotNull(message = "Ngày phát hành không được để trống")
    private LocalDate ngayPhatHanh;

    @NotNull(message = "Trạng thái phim không được để trống")
    private Enum.TrangThaiPhim trangThai;

    @NotBlank(message = "Định dạng phim không được để trống")
    @Pattern(regexp = "2D|3D|4D|IMAX", message = "Định dạng phải là 2D, 3D, 4D hoặc IMAX")
    private String dinhDang;

    @Pattern(
            regexp = "^(https?://)?(www\\.)?(youtube\\.com|youtu\\.be)/.+$",
            message = "URL trailer phải là liên kết hợp lệ đến YouTube"
    )
    private String trailerUrl;


    // Danh sách ID
    @NotEmpty(message = "Phim phải có ít nhất một thể loại")
    private Set<Integer> theLoaiIds;

    @NotEmpty(message = "Phim phải có ít nhất một đạo diễn")
    private Set<Integer> daoDienIds;

    @NotEmpty(message = "Phim phải có ít nhất một diễn viên")
    private Set<Integer> dienVienIds;

    // Tên các thực thể mới muốn thêm
    private Set<String> theLoaiMoi;

    private Set<String> daoDienMoi;

    private Set<String> dienVienMoi;
}