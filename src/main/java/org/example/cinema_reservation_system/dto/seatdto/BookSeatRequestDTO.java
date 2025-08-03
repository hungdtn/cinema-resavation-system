package org.example.cinema_reservation_system.dto.seatdto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class BookSeatRequestDTO {

        @NotEmpty(message = "Danh sách ghế không được trống")
        @Size(max = 8, message = "Chỉ được đặt tối đa 8 ghế một lần")
        private java.util.List<Integer> danhSachIdGheNgoi;

        @NotNull(message = "ID lịch chiếu không được null")
        private Integer idLichChieu;
    }
