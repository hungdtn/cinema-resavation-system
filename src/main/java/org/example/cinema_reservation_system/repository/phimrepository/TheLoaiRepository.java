package org.example.cinema_reservation_system.repository.phimrepository;

import org.example.cinema_reservation_system.entity.DienVien;
import org.example.cinema_reservation_system.entity.TheLoai;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TheLoaiRepository extends JpaRepository<TheLoai, Integer> {
    Optional<TheLoai> findByTenTheLoaiIgnoreCase(String ten);
}
