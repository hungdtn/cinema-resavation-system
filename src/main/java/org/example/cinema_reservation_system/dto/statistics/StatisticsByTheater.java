package org.example.cinema_reservation_system.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsByTheater {
    private String tenRapChieu;
    private BigDecimal tongTien;
    private Long soVeBan;


}
