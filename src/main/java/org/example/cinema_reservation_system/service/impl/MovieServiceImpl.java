package org.example.cinema_reservation_system.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.moviedto.MovieRequestDto;
import org.example.cinema_reservation_system.dto.moviedto.MovieResponseDto;
import org.example.cinema_reservation_system.entity.*;
import org.example.cinema_reservation_system.exception.ResourceNotFoundException;
import org.example.cinema_reservation_system.mapper.MovieMapper;
import org.example.cinema_reservation_system.repository.actor.ActorRepository;
import org.example.cinema_reservation_system.repository.director.DirectorRepository;
import org.example.cinema_reservation_system.repository.genre.GenreRepository;
import org.example.cinema_reservation_system.repository.image.ImageNativeRepository;
import org.example.cinema_reservation_system.repository.movie.*;
import org.example.cinema_reservation_system.repository.trailer.TrailerRepository;
import org.example.cinema_reservation_system.service.MovieService;
import org.example.cinema_reservation_system.service.storage.FileStorageService;
import org.example.cinema_reservation_system.utils.enums.LoaiHinhAnh;
import static org.example.cinema_reservation_system.validator.PhimValidator.validate;
import static org.example.cinema_reservation_system.validator.PhimValidator.validateImage;

import org.example.cinema_reservation_system.utils.enums.TrangThaiPhim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieServiceImpl implements MovieService {
    private final MovieRepository phimRepo;
    private final MovieMapper phimMapper;
    private final FileStorageService fileStorageService;
    private final DirectorRepository daoDienRepo;
    private final ActorRepository dienVienRepo;
    private final GenreRepository theLoaiRepo;
    private final ImageNativeRepository hinhAnhNativeRepo;
    private final EntityManager entityManager;
    private final TrailerRepository trailerRepository;

    @Override
    public List<MovieResponseDto> getAll() {
        return phimRepo.findAllFetchHinhAnh().stream()
                .map(phimMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public MovieResponseDto getById(Integer id) {
        Movie phim = phimRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phim với ID: " + id));
        return phimMapper.toDto(phim);
    }

    @Override
    public MovieResponseDto create(MovieRequestDto dto, MultipartFile poster, MultipartFile banner) {
        validateRequest(dto, poster, banner, LocalDate.now());
        Movie phim = saveOrUpdateMovieEntity(null, dto, poster, banner);
        return phimMapper.toDto(phim);
    }

    @Override
    public MovieResponseDto update(Integer id, MovieRequestDto dto, MultipartFile poster, MultipartFile banner) {
        Movie phim = phimRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phim với ID: " + id));
        validateRequest(dto, poster, banner, phim.getNgayTao());
        phim = saveOrUpdateMovieEntity(id, dto, poster, banner);
        return phimMapper.toDto(phim);
    }

    @Override
    public Map<String, Object> delete(Integer id) {
        Movie phim = phimRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phim với ID: " + id));

        phimRepo.delete(phim);

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Xóa phim thành công");
        result.put("idPhim", phim.getIdPhim());
        result.put("tenPhim", phim.getTenPhim());
        return result;
    }

    @Override
    public List<MovieResponseDto> filter(String theLoai, TrangThaiPhim trangThai) {
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

    private void validateRequest(MovieRequestDto dto, MultipartFile poster, MultipartFile banner, LocalDate ngayTao) {
        validate(dto, ngayTao);
        validateNames(dto);
        validateImage(poster, "Ảnh poster");
        validateImage(banner, "Ảnh banner");
    }

    private void validateNames(MovieRequestDto dto) {
        validateTextOnly(dto.getDaoDienMoi(), "Tên đạo diễn mới");
        validateTextOnly(dto.getDienVienMoi(), "Tên diễn viên mới");
        validateTextOnly(dto.getTheLoaiMoi(), "Tên thể loại mới");
    }

    private Movie saveOrUpdateMovieEntity(Integer id, MovieRequestDto dto, MultipartFile poster, MultipartFile banner) {
        if (dto.getTheLoaiIds() == null) {
            dto.setTheLoaiIds(new HashSet<>());
        }
        if (dto.getDaoDienIds() == null) {
            dto.setDaoDienIds(new HashSet<>());
        }
        if (dto.getDienVienIds() == null) {
            dto.setDienVienIds(new HashSet<>());
        }
        // Bước 1: Lưu hoặc thêm mới các entity liên quan nếu cần
        dto.getTheLoaiIds().addAll(saveIfNotExist(
                dto.getTheLoaiMoi(), theLoaiRepo,
                Genre::getTenTheLoai, name -> new Genre(null, name), Genre::getIdTheLoai
        ));
        dto.getDaoDienIds().addAll(saveIfNotExist(
                dto.getDaoDienMoi(), daoDienRepo,
                Director::getTenDaoDien, name -> new Director(null, name), Director::getIdDaoDien
        ));
        dto.getDienVienIds().addAll(saveIfNotExist(
                dto.getDienVienMoi(), dienVienRepo,
                Actor::getTenDienVien, name -> new Actor(null, name), Actor::getIdDienVien
        ));

        // Bước 2: Insert hoặc update phim
        LocalDate ngayTao = (id == null)
                ? LocalDate.now()
                : phimRepo.findById(id).orElseThrow().getNgayTao();

        if (id == null) {
            phimRepo.insertPhim(
                    dto.getTenPhim(), dto.getMoTa(), dto.getThoiLuong(),
                    dto.getNgayPhatHanh(), dto.getTrangThai().name(), dto.getDinhDang(), ngayTao
            );
        } else {
            phimRepo.updatePhim(
                    id, dto.getTenPhim(), dto.getMoTa(), dto.getThoiLuong(),
                    dto.getNgayPhatHanh(), dto.getTrangThai().name(), dto.getDinhDang(), ngayTao
            );
        }

        // Bước 3: Load phim từ DB
        Movie phim = phimRepo.findByTenPhim(dto.getTenPhim())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phim sau khi lưu"));

        // Bước 4: Gán quan hệ
        phim.setTheLoaiList(new HashSet<>(theLoaiRepo.findAllById(dto.getTheLoaiIds())));
        phim.setDaoDienList(new HashSet<>(daoDienRepo.findAllById(dto.getDaoDienIds())));
        phim.setDienVienList(new HashSet<>(dienVienRepo.findAllById(dto.getDienVienIds())));

        // Bước 5: Trailer
        saveOrUpdateTrailer(dto.getTrailerUrl(), phim);

        // Bước 6: Ảnh poster/banner
        saveImage(poster, LoaiHinhAnh.POSTER, phim);
        saveImage(banner, LoaiHinhAnh.BANNER, phim);

        entityManager.flush();
        entityManager.refresh(phim);

        return phim;
    }

    private void saveOrUpdateTrailer(String url, Movie phim) {
        if (url != null && !url.isBlank()) {
            Trailer trailer = Optional.ofNullable(phim.getTrailer()).orElse(new Trailer());
            trailer.setPhim(phim);
            trailer.setUrl(url);
            trailerRepository.save(trailer);
            phim.setTrailer(trailer);
        }
    }

    private void saveImage(MultipartFile file, LoaiHinhAnh loai, Movie phim) {
        if (file != null && !file.isEmpty()) {
            String folder = (loai == LoaiHinhAnh.POSTER) ? "posters" : "banners";
            String url = fileStorageService.saveFile(file, folder);

            hinhAnhNativeRepo.insertImage(
                    file.getOriginalFilename(),
                    loai.name(),
                    url,
                    phim.getIdPhim()
            );
        }
    }

    private <T, R> Set<R> saveIfNotExist(
            Set<String> tenMoi,
            JpaRepository<T, R> repo,
            Function<T, String> getTen,
            Function<String, T> createEntity,
            Function<T, R> getId
    ) {
        if (tenMoi == null || tenMoi.isEmpty()) return Collections.emptySet();

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

