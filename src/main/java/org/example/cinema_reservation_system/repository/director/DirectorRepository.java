package org.example.cinema_reservation_system.repository.director;

import org.example.cinema_reservation_system.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DirectorRepository extends JpaRepository<Director, Integer> {
    Optional<Director> findByTenDaoDienIgnoreCase(String ten);

}
