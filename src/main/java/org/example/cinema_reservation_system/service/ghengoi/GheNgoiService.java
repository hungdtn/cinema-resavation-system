package org.example.cinema_reservation_system.service.ghengoi;


import org.example.cinema_reservation_system.dto.ghengoidto.DatGheRequestDTO;
import org.example.cinema_reservation_system.dto.ghengoidto.GheNgoiDTO;
import org.example.cinema_reservation_system.dto.ghengoidto.SoDoGheDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface GheNgoiService {

    // CRUD Operations
    GheNgoiDTO create(GheNgoiDTO gheNgoiDTO);
    GheNgoiDTO update(Integer id, GheNgoiDTO gheNgoiDTO);
    GheNgoiDTO findById(Integer id);
    Page<GheNgoiDTO> findAll(Pageable pageable);
    void delete(Integer id);

    // Business Operations
    List<GheNgoiDTO> findByPhongChieu(Integer idPhongChieu);
    SoDoGheDTO getSoDoGhe(Integer idPhongChieu);
    List<GheNgoiDTO> findGheTrong(Integer idPhongChieu);

    // Đặt ghế operations
    List<GheNgoiDTO> datGhe(DatGheRequestDTO request);
    void xacNhanDatGhe(List<Integer> danhSachIdGheNgoi);
    void huyDatGhe(List<Integer> danhSachIdGheNgoi);

    // Bulk operations
    List<GheNgoiDTO> createBulkGhe(Integer idPhongChieu, String loaiGhe,
                                   String hangBatDau, int soHang, int soGheMoiHang);
    void deleteByPhongChieu(Integer idPhongChieu);

    // Utility methods
    Map<String, Long> getThongKeGhe(Integer idPhongChieu);
    boolean isGheAvailable(Integer idGheNgoi);
}