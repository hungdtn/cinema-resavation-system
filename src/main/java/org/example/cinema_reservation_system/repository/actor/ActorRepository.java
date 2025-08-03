package org.example.cinema_reservation_system.repository.actor;

import org.example.cinema_reservation_system.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
    Optional<Actor> findByTenDienVienIgnoreCase(String ten);
}
