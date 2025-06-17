package org.example.cinema_reservation_system.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.phimdto.PhimRequestDto;
import org.example.cinema_reservation_system.dto.phimdto.PhimResponseDto;
import org.example.cinema_reservation_system.entity.*;
import org.example.cinema_reservation_system.exception.ResourceNotFoundException;
import org.example.cinema_reservation_system.mapper.PhimMapper;
import org.example.cinema_reservation_system.repository.phimrepository.*;
import org.example.cinema_reservation_system.service.PhimService;
import org.example.cinema_reservation_system.service.storage.FileStorageService;
import org.example.cinema_reservation_system.utils.Enum;
import org.example.cinema_reservation_system.utils.LoaiHinhAnh;
import static org.example.cinema_reservation_system.validator.PhimValidator.validate;
import static org.example.cinema_reservation_system.validator.PhimValidator.validateImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PhimServiceImpl implements PhimService {
    private final PhimRepository phimRepo;
    private final PhimMapper phimMapper;
    private final FileStorageService fileStorageService;
    private final DaoDienRepository daoDienRepo;
    private final DienVienRepository dienVienRepo;
    private final TheLoaiRepository theLoaiRepo;
    private final HinhAnhNativeRepository hinhAnhNativeRepo;
    private final EntityManager entityManager;
    private final TrailerRepository trailerRepository;

    @Override
    public List<PhimResponseDto> getAll() {
        return phimRepo.findAllFetchHinhAnh().stream()
                .map(phimMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PhimResponseDto getById(Integer id) {
        Phim phim = phimRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phim với ID: " + id));
        return phimMapper.toDto(phim);
    }

    @Override
    public PhimResponseDto create(PhimRequestDto dto, MultipartFile poster, MultipartFile banner) {
        validate(dto, LocalDate.now());

        validateTextOnly(dto.getDaoDienMoi(), "Tên đạo diễn mới");
        validateTextOnly(dto.getDienVienMoi(), "Tên diễn viên mới");
        validateTextOnly(dto.getTheLoaiMoi(), "Tên thể loại mới");

        validateImage(poster, "Ảnh poster");
        validateImage(banner, "Ảnh banner");

        // Thêm mới thể loại, đạo diễn, diễn viên nếu chưa tồn tại
        dto.getTheLoaiIds().addAll(saveIfNotExist(
                dto.getTheLoaiMoi(),
                theLoaiRepo,
                TheLoai::getTenTheLoai,
                name -> new TheLoai(null, name),
                TheLoai::getIdTheLoai
        ));

        dto.getDaoDienIds().addAll(saveIfNotExist(
                dto.getDaoDienMoi(),
                daoDienRepo,
                DaoDien::getTenDaoDien,
                name -> new DaoDien(null, name),
                DaoDien::getIdDaoDien
        ));

        dto.getDienVienIds().addAll(saveIfNotExist(
                dto.getDienVienMoi(),
                dienVienRepo,
                DienVien::getTenDienVien,
                name -> new DienVien(null, name),
                DienVien::getIdDienVien
        ));

        // Tạo phim
        LocalDateTime ngayTao = LocalDateTime.now();
        phimRepo.insertPhim(
                dto.getTenPhim(),
                dto.getMoTa(),
                dto.getThoiLuong(),
                dto.getNgayPhatHanh(),
                dto.getTrangThai().name(),
                dto.getDinhDang(),
                ngayTao
        );

        Phim phim = phimRepo.findByTenPhim(dto.getTenPhim())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phim sau khi thêm"));

        phim.setTheLoaiList(new HashSet<>(theLoaiRepo.findAllById(dto.getTheLoaiIds())));
        phim.setDaoDienList(new HashSet<>(daoDienRepo.findAllById(dto.getDaoDienIds())));
        phim.setDienVienList(new HashSet<>(dienVienRepo.findAllById(dto.getDienVienIds())));

        if (dto.getTrailerUrl() != null && !dto.getTrailerUrl().isBlank()) {
            Trailer trailer = new Trailer();
            trailer.setUrl(dto.getTrailerUrl());
            trailer.setPhim(phim); // set quan hệ 1-1
            trailerRepository.save(trailer);
            phim.setTrailer(trailer); // liên kết lại trong entity nếu cần map trả về
        }

        saveImage(poster, LoaiHinhAnh.POSTER, phim);
        saveImage(banner, LoaiHinhAnh.BANNER, phim);

        entityManager.flush();
        entityManager.refresh(phim);

        return phimMapper.toDto(phim);
    }

    @Override
    public PhimResponseDto update(Integer id, PhimRequestDto dto, MultipartFile poster, MultipartFile banner) {
        Phim phim = phimRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phim với ID: " + id));

        validate(dto, LocalDate.from(phim.getNgayTao()));

        validateTextOnly(dto.getDaoDienMoi(), "Tên đạo diễn mới");
        validateTextOnly(dto.getDienVienMoi(), "Tên diễn viên mới");
        validateTextOnly(dto.getTheLoaiMoi(), "Tên thể loại mới");

        validateImage(poster, "Ảnh poster");
        validateImage(banner, "Ảnh banner");

        // Thêm mới nếu cần
        dto.getTheLoaiIds().addAll(saveIfNotExist(
                dto.getTheLoaiMoi(),
                theLoaiRepo,
                TheLoai::getTenTheLoai,
                name -> new TheLoai(null, name),
                TheLoai::getIdTheLoai
        ));

        dto.getDaoDienIds().addAll(saveIfNotExist(
                dto.getDaoDienMoi(),
                daoDienRepo,
                DaoDien::getTenDaoDien,
                name -> new DaoDien(null, name),
                DaoDien::getIdDaoDien
        ));

        dto.getDienVienIds().addAll(saveIfNotExist(
                dto.getDienVienMoi(),
                dienVienRepo,
                DienVien::getTenDienVien,
                name -> new DienVien(null, name),
                DienVien::getIdDienVien
        ));

        // Cập nhật phim
        LocalDateTime ngayTao = phim.getNgayTao(); // giữ nguyên ngày tạo cũ
        phimRepo.updatePhim(
                id,
                dto.getTenPhim(),
                dto.getMoTa(),
                dto.getThoiLuong(),
                dto.getNgayPhatHanh(),
                dto.getTrangThai().name(),
                dto.getDinhDang(),
                ngayTao
        );

        phim.setTheLoaiList(new HashSet<>(theLoaiRepo.findAllById(dto.getTheLoaiIds())));
        phim.setDaoDienList(new HashSet<>(daoDienRepo.findAllById(dto.getDaoDienIds())));
        phim.setDienVienList(new HashSet<>(dienVienRepo.findAllById(dto.getDienVienIds())));

        if (dto.getTrailerUrl() != null && !dto.getTrailerUrl().isBlank()) {
            Trailer trailer = phim.getTrailer(); // trailer hiện tại

            if (trailer == null) {
                trailer = new Trailer();
                trailer.setPhim(phim);
            }

            trailer.setUrl(dto.getTrailerUrl());
            trailerRepository.save(trailer);
            phim.setTrailer(trailer);
        }

        saveImage(poster, LoaiHinhAnh.POSTER, phim);
        saveImage(banner, LoaiHinhAnh.BANNER, phim);

        entityManager.flush();
        entityManager.refresh(phim);

        return phimMapper.toDto(phim);
    }

    @Override
    public Map<String, Object> delete(Integer id) {
        Phim phim = phimRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phim với ID: " + id));

        phimRepo.delete(phim);

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Xóa phim thành công");
        result.put("idPhim", phim.getIdPhim());
        result.put("tenPhim", phim.getTenPhim());
        return result;
    }

    @Override
    public List<PhimResponseDto> filter(String theLoai, Enum.TrangThaiPhim trangThai) {
        return phimRepo.findAll().stream()
                .filter(phim -> {
                    boolean matchTrangThai = trangThai == null || phim.getTrangThai() == trangThai;
                    boolean matchTheLoai = true;
                    if (theLoai != null && !theLoai.isBlank()) {
                        matchTheLoai = phim.getTheLoaiList().stream()
                                .anyMatch(tl -> tl.getTenTheLoai().equalsIgnoreCase(theLoai));
                    }
                    return matchTrangThai && matchTheLoai;
                })
                .map(phimMapper::toDto)
                .collect(Collectors.toList());
    }

    private void saveImage(MultipartFile file, LoaiHinhAnh loai, Phim phim) {
        if (file != null && !file.isEmpty()) {
            String folder = loai == LoaiHinhAnh.POSTER ? "posters" : "banners";
            String url = fileStorageService.saveFile(file, folder);

            hinhAnhNativeRepo.insertImage(
                    file.getOriginalFilename(),
                    loai.name(),
                    url,
                    phim.getIdPhim()
            );
        }
    }

    //Phương thức dùng để add & update daoDien, dienVien, theLoai mới
    private <T, R> Set<R> saveIfNotExist(
            Set<String> tenMoi,
            JpaRepository<T, R> repo,
            Function<T, String> getTen,
            Function<String, T> createEntity,
            Function<T, R> getId
    ) {
        List<T> all = repo.findAll();
        Set<R> ids = all.stream()
                .filter(e -> tenMoi.contains(getTen.apply(e)))
                .map(getId)
                .collect(Collectors.toSet());

        tenMoi.stream()
                .filter(name -> all.stream().noneMatch(e -> getTen.apply(e).equalsIgnoreCase(name)))
                .map(createEntity)
                .map(repo::save)
                .map(getId)
                .forEach(ids::add);

        return ids;
    }

    private void validateTextOnly(Set<String> values, String fieldName) {
        if (values == null) return;

        for (String value : values) {
            if (value != null && !value.matches("^[\\p{L}\\s]+$")) {
                throw new IllegalArgumentException(fieldName + " phải là chữ, không chứa số hoặc ký tự đặc biệt: " + value);
            }
        }
    }
}

