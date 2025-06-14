package org.example.cinema_reservation_system.repository;

import org.example.cinema_reservation_system.entity.SuatChieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuatChieuRepo extends JpaRepository<SuatChieu, Integer> {
}
