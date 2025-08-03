package org.example.cinema_reservation_system.service.seat;


import org.example.cinema_reservation_system.dto.seatdto.BookSeatRequestDTO;
import org.example.cinema_reservation_system.dto.seatdto.SeatDTO;
import org.example.cinema_reservation_system.dto.seatdto.SeatingChartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface SeatService {

    // CRUD Operations
    SeatDTO create(SeatDTO gheNgoiDTO);
    SeatDTO update(Integer id, SeatDTO gheNgoiDTO);
    SeatDTO findById(Integer id);
    Page<SeatDTO> findAll(Pageable pageable);
    void delete(Integer id);

    // Business Operations
    List<SeatDTO> findByPhongChieu(Integer idPhongChieu);
    SeatingChartDTO getSoDoGhe(Integer idPhongChieu);
    List<SeatDTO> findGheTrong(Integer idPhongChieu);

    // Đặt ghế operations
    List<SeatDTO> datGhe(BookSeatRequestDTO request);
    void xacNhanDatGhe(List<Integer> danhSachIdGheNgoi);
    void huyDatGhe(List<Integer> danhSachIdGheNgoi);

    // Bulk operations
    List<SeatDTO> createBulkGhe(Integer idPhongChieu, String loaiGhe,
                                String hangBatDau, int soHang, int soGheMoiHang);
    void deleteByPhongChieu(Integer idPhongChieu);

    // Utility methods
    Map<String, Long> getThongKeGhe(Integer idPhongChieu);
    boolean isGheAvailable(Integer idGheNgoi);
}