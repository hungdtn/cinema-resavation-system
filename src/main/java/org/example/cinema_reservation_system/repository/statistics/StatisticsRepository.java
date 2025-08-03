package org.example.cinema_reservation_system.repository.statistics;

import org.example.cinema_reservation_system.dto.statistics.*;
import org.example.cinema_reservation_system.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatisticsRepository extends JpaRepository<Invoice, Long> {
}
