package org.example.cinema_reservation_system.repository.phimrepository;

import org.example.cinema_reservation_system.entity.DaoDien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DaoDienRepository extends JpaRepository<DaoDien, Integer> {
    Optional<DaoDien> findByTenDaoDienIgnoreCase(String ten);

}
