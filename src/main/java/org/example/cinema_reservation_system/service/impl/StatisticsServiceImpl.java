package org.example.cinema_reservation_system.service.impl;

import org.example.cinema_reservation_system.dto.statistics.StatisticsByMovie;
import org.example.cinema_reservation_system.dto.statistics.StatisticsByTheater;
import org.example.cinema_reservation_system.repository.invoice.InvoiceRepository;
import org.example.cinema_reservation_system.service.StatisticsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final InvoiceRepository hoaDonRepo;

    public StatisticsServiceImpl(InvoiceRepository hoaDonRepo) {
        this.hoaDonRepo = hoaDonRepo;
    }

    @Override
    public Double getTongDoanhThu() {
        return hoaDonRepo.getTongDoanhThu() != null ? hoaDonRepo.getTongDoanhThu() : 0.0;
    }

    @Override
    public BigDecimal tongdoanhThuThangNam(int month, int year) {
        return hoaDonRepo.tongDoanhThuTheoThangNam(year, month);
    }

    @Override
    public List<StatisticsByTheater> getDoanhThuTheoRap() {
        List<Object[]> results = hoaDonRepo.getDoanhThuTheoRap();

        // Debug log
        for (Object[] row : results) {
            System.out.println(Arrays.toString(row));
        }

        // Map về DTO để trả ra JSON đẹp
        return results.stream()
                .map(r -> new StatisticsByTheater(
                        (String) r[0],
                        (BigDecimal) r[1],
                        ((Long) r[2]).longValue() // soVeBan
                ))
                .toList();
    }

    @Override
    public List<StatisticsByMovie> getDoanhThuTheoPhim() {
        List<Object[]> results = hoaDonRepo.getDoanhThuTheoPhim();

        for (Object[] row : results) {
            System.out.println(Arrays.toString(row));
        }

        return results.stream()
                .map(r -> new StatisticsByMovie(
                        (String) r[0],                // tenPhim
                        (BigDecimal) r[1],            // tongDoanhThu
                        ((Number) r[2]).longValue()   // soVeBan
                ))
                .toList();
    }


}
