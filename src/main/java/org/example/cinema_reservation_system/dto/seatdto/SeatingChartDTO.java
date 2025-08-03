package org.example.cinema_reservation_system.dto.seatdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cinema_reservation_system.utils.enums.TrangThaiGheNgoi;

// DTO cho sơ đồ ghế - chỉ dùng khi cần hiển thị sơ đồ
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatingChartDTO {
    private Integer idPhongChieu;
    private String tenPhongChieu;
    private java.util.List<GheInSoDoDTO> danhSachGhe;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GheInSoDoDTO {
        private Integer idGheNgoi;
        private String hangGhe;
        private String soGhe;
        private String viTriGhe;
        private TrangThaiGheNgoi trangThai;
        private String loaiGhe;
        private boolean available;
    }
}
