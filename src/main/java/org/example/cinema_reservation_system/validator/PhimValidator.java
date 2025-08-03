package org.example.cinema_reservation_system.validator;

import org.example.cinema_reservation_system.dto.moviedto.MovieRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class PhimValidator {
    public static void validate(MovieRequestDto dto, LocalDate ngayTao) {
        if (dto.getTenPhim() == null || dto.getTenPhim().isBlank()) {
            throw new IllegalArgumentException("Tên phim không được để trống.");
        }

        if (dto.getMoTa() == null || dto.getMoTa().isBlank()) {
            throw new IllegalArgumentException("Mô tả không được để trống.");
        }

        if (dto.getThoiLuong() == null) {
            throw new IllegalArgumentException("Thời lượng phim không được để trống.");
        }

        if (dto.getThoiLuong() <= 0) {
            throw new IllegalArgumentException("Thời lượng phim phải lớn hơn 0.");
        }


        if (dto.getNgayPhatHanh() == null) {
            throw new IllegalArgumentException("Ngày phát hành không được để trống.");
        }

        // So sánh với ngày tạo thật sự (không phải hôm nay)
        if (dto.getNgayPhatHanh().isBefore(ngayTao)) {
            throw new IllegalArgumentException("Ngày phát hành không được nhỏ hơn ngày tạo.");
        }

        if (dto.getDinhDang() == null || dto.getDinhDang().isBlank()) {
            throw new IllegalArgumentException("Định dạng phim không được để trống.");
        }

        if (!dto.getDinhDang().matches("2D|3D|4D|IMAX")) {
            throw new IllegalArgumentException("Định dạng phải là 2D, 3D, 4D hoặc IMAX.");
        }

        if (dto.getTrangThai() == null) {
            throw new IllegalArgumentException("Trạng thái phim không được để trống.");
        }

        if ((dto.getTheLoaiIds() == null || dto.getTheLoaiIds().isEmpty()) &&
                (dto.getTheLoaiMoi() == null || dto.getTheLoaiMoi().isEmpty())) {
            throw new IllegalArgumentException("Phim phải có ít nhất một thể loại.");
        }

        if ((dto.getDaoDienIds() == null || dto.getDaoDienIds().isEmpty()) &&
                (dto.getDaoDienMoi() == null || dto.getDaoDienMoi().isEmpty())) {
            throw new IllegalArgumentException("Phim phải có ít nhất một đạo diễn.");
        }

        if ((dto.getDienVienIds() == null || dto.getDienVienIds().isEmpty()) &&
                (dto.getDienVienMoi() == null || dto.getDienVienMoi().isEmpty())) {
            throw new IllegalArgumentException("Phim phải có ít nhất một diễn viên.");
        }
    }

    public static void validateImage(MultipartFile file, String label) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(label + " không được để trống.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException(label + " phải là file ảnh (jpeg, png, ...).");
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            throw new IllegalArgumentException(label + " không được lớn hơn 5MB.");
        }
    }

}
