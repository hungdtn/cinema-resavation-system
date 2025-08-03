package org.example.cinema_reservation_system.repository.cinematicket;

import org.example.cinema_reservation_system.entity.Seat;
import org.example.cinema_reservation_system.entity.ShowTime;
import org.example.cinema_reservation_system.entity.CinemaTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaTicketRepository extends JpaRepository<CinemaTicket, Integer>, CinemaTicketRepositoryCustom {

    boolean existsByGheNgoiAndSuatChieu(Seat gheNgoi, ShowTime suatChieu);
    List<CinemaTicket> findAllByHoaDon_IdHoaDon(Integer idHoaDon);

}
