package org.example.cinema_reservation_system.repository;

import org.example.cinema_reservation_system.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonRepo extends JpaRepository<HoaDon, Integer> {
    @Query("SELECT SUM(h.tongTien) FROM HoaDon h WHERE h.trangThai = 'da_thanh_toan'")
    Double getTongDoanhThu();

}
