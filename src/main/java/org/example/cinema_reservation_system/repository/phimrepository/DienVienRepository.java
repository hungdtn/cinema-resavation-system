package org.example.cinema_reservation_system.repository.phimrepository;

import org.example.cinema_reservation_system.entity.DienVien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DienVienRepository extends JpaRepository<DienVien, Integer> {
    Optional<DienVien> findByTenDienVienIgnoreCase(String ten);
}
