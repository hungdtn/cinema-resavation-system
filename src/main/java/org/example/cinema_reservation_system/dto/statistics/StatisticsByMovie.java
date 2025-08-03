package org.example.cinema_reservation_system.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsByMovie {
    private String tenPhim;
    private BigDecimal tongDoanhThu;
    private Long soVeBan;
}
