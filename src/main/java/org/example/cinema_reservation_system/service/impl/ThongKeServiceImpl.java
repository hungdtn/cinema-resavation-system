package org.example.cinema_reservation_system.service.impl;

import org.example.cinema_reservation_system.repository.HoaDonRepo;
import org.example.cinema_reservation_system.service.ThongKeService;
import org.springframework.stereotype.Service;

@Service
public class ThongKeServiceImpl implements ThongKeService {
    private final HoaDonRepo hoaDonRepo;

    public ThongKeServiceImpl(HoaDonRepo hoaDonRepo) {
        this.hoaDonRepo = hoaDonRepo;
    }

    @Override
    public Double getTongDoanhThu() {
        return hoaDonRepo.getTongDoanhThu() != null ? hoaDonRepo.getTongDoanhThu() : 0.0;
    }
}
