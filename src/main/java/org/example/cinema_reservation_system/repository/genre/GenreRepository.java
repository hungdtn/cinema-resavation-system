package org.example.cinema_reservation_system.repository.genre;

import org.example.cinema_reservation_system.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Optional<Genre> findByTenTheLoaiIgnoreCase(String ten);
}
