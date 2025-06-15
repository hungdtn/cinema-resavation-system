package org.example.cinema_reservation_system.repository.phimrepository;

import org.example.cinema_reservation_system.entity.Trailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrailerRepository extends JpaRepository<Trailer,Integer> {
    Optional<Trailer>findByPhim_IdPhim(Integer idPhim);
}
