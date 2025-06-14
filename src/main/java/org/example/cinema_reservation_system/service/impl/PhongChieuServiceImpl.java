package org.example.cinema_reservation_system.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.phongchieudto.PhongChieuRequestDto;
import org.example.cinema_reservation_system.dto.phongchieudto.PhongChieuResponseDto;
import org.example.cinema_reservation_system.entity.PhongChieu;
import org.example.cinema_reservation_system.entity.RapChieu;
import org.example.cinema_reservation_system.exception.ResourceNotFoundException;
import org.example.cinema_reservation_system.repository.phongchieurepository.PhongChieuRepo;
import org.example.cinema_reservation_system.repository.rapchieurepository.RapChieuRepository;
import org.example.cinema_reservation_system.service.PhongChieuService;
import org.example.cinema_reservation_system.utils.Enum.TrangThaiPhongChieu;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhongChieuServiceImpl implements PhongChieuService {

    private final PhongChieuRepo phongChieuRepo;
    private final RapChieuRepository rapChieuRepo;
    private final ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PhongChieuResponseDto> findAllDto() {
        return phongChieuRepo.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public PhongChieuResponseDto findByIdDto(Integer id) {
        PhongChieu pc = getPhongChieuById(id);
        return toResponseDto(pc);
    }

    @Override
    @Transactional
    public PhongChieuResponseDto savePhongChieu(PhongChieuRequestDto dto) {
        // Bước 1: Insert bằng native query
        phongChieuRepo.insertPhongChieu(
                dto.getTenPhongChieu(),
                dto.getDienTichPhong(),
                dto.getTrangThaiPhongChieu().name(),
                dto.getIdRapChieu()
        );

        // Bước 2: Lấy lại phòng chiếu vừa thêm (dựa vào tên + idRap để tìm)
        // * giả định tên phòng chiếu là duy nhất trong 1 rạp
        PhongChieu entity = phongChieuRepo.findTopByTenPhongChieuAndRapChieu_IdRapChieuOrderByIdPhongChieuDesc(
                dto.getTenPhongChieu(), dto.getIdRapChieu()
        ).orElseThrow(() -> new RuntimeException("Không tìm thấy phòng chiếu vừa thêm"));

        // Bước 3: Trả về DTO chi tiết
        RapChieu rap = entity.getRapChieu();
        return new PhongChieuResponseDto(
                entity.getId(),
                entity.getTenPhongChieu(),
                entity.getDienTichPhong(),
                entity.getTrangThai(),
                rap.getIdRapChieu(),
                rap.getTenRapChieu(),
                rap.getDiaChi(),
                rap.getSoDienThoai(),
                rap.getTrangThai()
        );
    }

    @Override
    @Transactional
    public PhongChieuResponseDto updatePhongChieu(Integer id, PhongChieuRequestDto dto) {
        getPhongChieuById(id); // ktra tồn tại

        //gọi native query để cập nhật
        phongChieuRepo.updatePhongChieu(
                id,
                dto.getTenPhongChieu(),
                dto.getDienTichPhong(),
                dto.getTrangThaiPhongChieu().name(), //truyền chuôi enum
                dto.getIdRapChieu()
        );

        entityManager.clear();  //Xóa persistence context để Hibernate đọc lại từ DB
        // Lấy lại dữ liệu sau update để trả về responseDTO
        PhongChieu updated = phongChieuRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sau update"));

        return toResponseDto(updated);
    }

    @Override
    @Transactional
    public void changeTrangThai(Integer id, TrangThaiPhongChieu trangThai) {
        // Kiểm tra tồn tại trước khi cập nhật để tránh silent fail
        if (!phongChieuRepo.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy phòng chiếu với ID: " + id);
        }

        phongChieuRepo.changeTrangThai(id, trangThai.name());
    }

    @Override
    public List<PhongChieuResponseDto> filterPhongChieu(
            Integer idRap,
            String trangThaiRaw,
            String tenPhongChieu,
            Double dienTichMin,
            Double dienTichMax,
            String sortBy,
            String order
    ) {
        Comparator<PhongChieu> comparator = getComparator(sortBy, order);

        return phongChieuRepo.findAll().stream()
                .filter(p -> filterByRap(p, idRap))
                .filter(p -> filterByTrangThai(p, trangThaiRaw))
                .filter(p -> filterByTenPhong(p, tenPhongChieu))
                .filter(p -> filterByDienTichMin(p, dienTichMin))
                .filter(p -> filterByDienTichMax(p, dienTichMax))
                .sorted(comparator)
                .map(this::toResponseDto)
                .toList();
    }

    private Comparator<PhongChieu> getComparator(String sortBy, String order) {
        Comparator<PhongChieu> comparator = Comparator.comparing(PhongChieu::getId); // mặc định

        if ("dienTich".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(PhongChieu::getDienTichPhong);
        } else if ("ten".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(p -> p.getTenPhongChieu().toLowerCase());
        } else if ("trangThai".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(p -> p.getTrangThai().name());
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    private PhongChieu getPhongChieuById(Integer id) {
        return phongChieuRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng chiếu với ID = " + id));
    }

    private RapChieu getRapChieuById(Integer id) {
        return rapChieuRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy rạp chiếu với ID = " + id));
    }

    private PhongChieu toEntity(PhongChieuRequestDto dto) {
        PhongChieu pc = new PhongChieu();
        pc.setId(dto.getIdPhongChieu());
        pc.setTenPhongChieu(dto.getTenPhongChieu());
        pc.setDienTichPhong(dto.getDienTichPhong());
        pc.setTrangThai(dto.getTrangThaiPhongChieu());
        pc.setRapChieu(getRapChieuById(dto.getIdRapChieu()));
        return pc;
    }

    private void updateEntityFromDto(PhongChieu pc, PhongChieuRequestDto dto) {
        pc.setTenPhongChieu(dto.getTenPhongChieu());
        pc.setDienTichPhong(dto.getDienTichPhong());
        pc.setTrangThai(dto.getTrangThaiPhongChieu());
        pc.setRapChieu(getRapChieuById(dto.getIdRapChieu()));
    }

    private PhongChieuResponseDto toResponseDto(PhongChieu pc) {
        RapChieu rc = pc.getRapChieu();
        return new PhongChieuResponseDto(
                pc.getId(),
                pc.getTenPhongChieu(),
                pc.getDienTichPhong(),
                pc.getTrangThai(),
                rc.getIdRapChieu(),
                rc.getTenRapChieu(),
                rc.getDiaChi(),
                rc.getSoDienThoai(),
                rc.getTrangThai()
        );
    }

    private boolean filterByRap(PhongChieu p, Integer idRap) {
        return idRap == null || p.getRapChieu().getIdRapChieu().equals(idRap);
    }

    private boolean filterByTrangThai(PhongChieu p, String trangThaiStr) {
        if (trangThaiStr == null) return true;
        try {
            return p.getTrangThai().name().equalsIgnoreCase(trangThaiStr);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean filterByTenPhong(PhongChieu p, String tenPhongChieu) {
        return tenPhongChieu == null || p.getTenPhongChieu().toLowerCase().contains(tenPhongChieu.toLowerCase());
    }

    private boolean filterByDienTichMin(PhongChieu p, Double dienTichMin) {
        return dienTichMin == null || p.getDienTichPhong() >= dienTichMin;
    }

    private boolean filterByDienTichMax(PhongChieu p, Double dienTichMax) {
        return dienTichMax == null || p.getDienTichPhong() <= dienTichMax;
    }

}
