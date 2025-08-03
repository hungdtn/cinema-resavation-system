package org.example.cinema_reservation_system.service;

import org.example.cinema_reservation_system.dto.statistics.*;

import java.math.BigDecimal;
import java.util.List;

public interface StatisticsService {
    Double getTongDoanhThu();
    BigDecimal tongdoanhThuThangNam(int thang, int nam);
    List<StatisticsByTheater> getDoanhThuTheoRap();
    List<StatisticsByMovie> getDoanhThuTheoPhim();
}
