package org.example.cinema_reservation_system.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.roomdto.RoomRequestDto;
import org.example.cinema_reservation_system.dto.roomdto.RoomResponseDto;
import org.example.cinema_reservation_system.entity.Room;
import org.example.cinema_reservation_system.entity.Theater;
import org.example.cinema_reservation_system.exception.ResourceNotFoundException;
import org.example.cinema_reservation_system.repository.room.RoomRepository;
import org.example.cinema_reservation_system.repository.theater.TheaterRepository;
import org.example.cinema_reservation_system.service.RoomService;
import org.example.cinema_reservation_system.utils.enums.TrangThaiPhongChieu;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepo;
    private final TheaterRepository theaterRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RoomResponseDto> findAllDto() {
        return roomRepo.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponseDto findByIdDto(Integer id) {
        Room room = getRoomById(id);
        return toResponseDto(room);
    }

    @Override
    @Transactional
    public RoomResponseDto savePhongChieu(RoomRequestDto dto) {
        roomRepo.insertPhongChieu(
                dto.getTenPhongChieu(),
                BigDecimal.valueOf(dto.getDienTichPhong()),
                dto.getTrangThaiPhongChieu().name(),
                dto.getIdRapChieu()
        );

        Room entity = roomRepo.findTopByTenPhongChieuAndRapChieu_IdRapChieuOrderByIdPhongChieuDesc(
                        dto.getTenPhongChieu(), dto.getIdRapChieu())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng chiếu vừa thêm"));

        return toResponseDto(entity);
    }

    @Override
    @Transactional
    public RoomResponseDto updatePhongChieu(Integer id, RoomRequestDto dto) {
        getRoomById(id); // check tồn tại

        roomRepo.updatePhongChieu(
                id,
                dto.getTenPhongChieu(),
                BigDecimal.valueOf(dto.getDienTichPhong()),
                dto.getTrangThaiPhongChieu().name(),
                dto.getIdRapChieu()
        );

        entityManager.clear();
        Room updated = getRoomById(id);
        return toResponseDto(updated);
    }

    @Override
    @Transactional
    public void changeTrangThai(Integer id, TrangThaiPhongChieu trangThai) {
        if (!roomRepo.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy phòng chiếu với ID: " + id);
        }
        roomRepo.changeTrangThai(id, trangThai.name());
    }

    @Override
    public List<RoomResponseDto> filterPhongChieu(
            Integer idRap,
            String trangThaiRaw,
            String tenPhongChieu,
            Double dienTichMin,
            Double dienTichMax,
            String sortBy,
            String order
    ) {
        Predicate<Room> combinedFilter = buildFilter(
                idRap, trangThaiRaw, tenPhongChieu,
                dienTichMin, dienTichMax
        );

        Comparator<Room> comparator = buildComparator(sortBy, order);

        return roomRepo.findAll().stream()
                .filter(combinedFilter)
                .sorted(comparator)
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /** ========== PRIVATE HELPERS ========== */

    private Room getRoomById(Integer id) {
        return roomRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng chiếu với ID = " + id));
    }

    private Theater getTheaterById(Integer id) {
        return theaterRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy rạp chiếu với ID = " + id));
    }

    private RoomResponseDto toResponseDto(Room room) {
        Theater theater = room.getRapChieu();
        return new RoomResponseDto(
                room.getIdPhongChieu(),
                room.getTenPhongChieu(),
                room.getDienTichPhong(),
                room.getTrangThai(),
                theater.getIdRapChieu(),
                theater.getTenRapChieu(),
                theater.getDiaChi(),
                theater.getSoDienThoai(),
                theater.getTrangThai()
        );
    }

    private Predicate<Room> buildFilter(
            Integer idRap,
            String trangThaiStr,
            String tenPhong,
            Double dienTichMin,
            Double dienTichMax
    ) {
        return room -> {
            if (idRap != null && !room.getRapChieu().getIdRapChieu().equals(idRap)) return false;

            if (trangThaiStr != null && !trangThaiStr.isBlank()) {
                try {
                    if (!room.getTrangThai().name().equalsIgnoreCase(trangThaiStr)) return false;
                } catch (Exception ignored) {
                    return false;
                }
            }

            if (tenPhong != null && !room.getTenPhongChieu().toLowerCase().contains(tenPhong.toLowerCase()))
                return false;

            if (dienTichMin != null && room.getDienTichPhong().compareTo(BigDecimal.valueOf(dienTichMin)) < 0)
                return false;

            if (dienTichMax != null && room.getDienTichPhong().compareTo(BigDecimal.valueOf(dienTichMax)) > 0)
                return false;

            return true;
        };
    }

    private Comparator<Room> buildComparator(String sortBy, String order) {
        Comparator<Room> comparator = Comparator.comparing(Room::getIdPhongChieu);

        if ("dienTich".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Room::getDienTichPhong);
        } else if ("ten".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(r -> r.getTenPhongChieu().toLowerCase());
        } else if ("trangThai".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(r -> r.getTrangThai().name());
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}
