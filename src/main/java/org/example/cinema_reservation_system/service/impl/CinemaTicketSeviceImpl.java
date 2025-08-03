package org.example.cinema_reservation_system.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.cinema_reservation_system.dto.cinematicketdto.CinemaTicketResponseDto;
import org.example.cinema_reservation_system.dto.cinematicketdto.CinemaTicketRequestDto;
import org.example.cinema_reservation_system.entity.*;
import org.example.cinema_reservation_system.exception.BadRequestException;
import org.example.cinema_reservation_system.exception.ResourceNotFoundException;
import org.example.cinema_reservation_system.repository.seat.SeatRepository;
import org.example.cinema_reservation_system.repository.invoice.InvoiceRepository;
import org.example.cinema_reservation_system.repository.customer.CustomerRepository;
import org.example.cinema_reservation_system.repository.staff.StaffRepository;
import org.example.cinema_reservation_system.repository.movie.MovieRepository;
import org.example.cinema_reservation_system.repository.room.RoomRepository;
import org.example.cinema_reservation_system.repository.showtime.ShowTimeRepository;
import org.example.cinema_reservation_system.repository.payment.PaymentRepository;
import org.example.cinema_reservation_system.repository.cinematicket.CinemaTicketRepository;
import org.example.cinema_reservation_system.service.CinemaTicketService;
import org.example.cinema_reservation_system.utils.enums.TrangThaiVePhim;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CinemaTicketSeviceImpl implements CinemaTicketService {
    private final CinemaTicketRepository vePhimRepo;
    private final SeatRepository gheNgoiRepository;
    private final MovieRepository phimRepository;
    private final ShowTimeRepository suatChieuRepository;
    private final CustomerRepository khachHangRepository;
    private final StaffRepository nhanVienRepository;
    private final RoomRepository phongChieuRepository;
    private final InvoiceRepository hoaDonRepository;
    private final PaymentRepository thanhToanRepository;
    private ModelMapper modelMapper;

    public CinemaTicketSeviceImpl(CinemaTicketRepository vePhimRepo, ModelMapper modelMapper, SeatRepository gheNgoiRepository, MovieRepository phimRepository, ShowTimeRepository suatChieuRepository, CustomerRepository khachHangRepository, StaffRepository nhanVienRepository, RoomRepository phongChieuRepository, InvoiceRepository hoaDonRepository, PaymentRepository thanhToanRepository) {
        this.vePhimRepo = vePhimRepo;
        this.modelMapper = modelMapper;

        // Create TypeMap for VePhim to VePhimDto
        TypeMap<CinemaTicket, CinemaTicketResponseDto> propertyTypeMap = modelMapper.createTypeMap(CinemaTicket.class, CinemaTicketResponseDto.class);
        propertyTypeMap.addMappings(mapper -> {
            mapper.map(src -> src.getPhim().getTenPhim(), CinemaTicketResponseDto::setTenPhim);
            mapper.map(src -> src.getNhanVien().getTenNhanVien(), CinemaTicketResponseDto::setTenNhanVien);
            mapper.map(src -> src.getSuatChieu().getNgayChieu(), CinemaTicketResponseDto::setNgayChieu);
            mapper.map(src -> src.getSuatChieu().getGioChieu(), CinemaTicketResponseDto::setGioChieu);
            mapper.map(src -> src.getGheNgoi().getSoGhe(), CinemaTicketResponseDto::setSoGhe);
            mapper.map(src -> src.getSuatChieu().getTenSuatChieu(), CinemaTicketResponseDto::setTenSuatChieu);
            mapper.map(src -> src.getKhachHang().getTenKhachHang(), CinemaTicketResponseDto::setTenKhachHang);
            mapper.map(src -> src.getKhachHang().getSoDienThoaiKhachHang(), CinemaTicketResponseDto::setSoDienThoaiKhachHang);
            mapper.map(src -> src.getSuatChieu().getPhongChieu().getRapChieu().getTenRapChieu(), CinemaTicketResponseDto::setTenRapChieu);
        });
        this.gheNgoiRepository = gheNgoiRepository;
        this.phimRepository = phimRepository;
        this.suatChieuRepository = suatChieuRepository;
        this.khachHangRepository = khachHangRepository;
        this.nhanVienRepository = nhanVienRepository;
        this.phongChieuRepository = phongChieuRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.thanhToanRepository = thanhToanRepository;
    }

    @Override
    public List<CinemaTicketResponseDto> getAllVePhim() {
        return vePhimRepo.findAll().stream()
                .map(vePhim -> modelMapper.map(vePhim, CinemaTicketResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CinemaTicketResponseDto createVePhim(CinemaTicketRequestDto dto) {
        // 1. Validate & truy vấn các entity liên quan
        Seat gheNgoi = gheNgoiRepository.findById(dto.getIdGheNgoi())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ghế ngồi"));

        Movie phim = phimRepository.findById(dto.getIdPhim())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phim"));

        ShowTime suatChieu = suatChieuRepository.findById(dto.getIdSuatChieu())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy suất chiếu"));

        Customer khachHang = khachHangRepository.findById(dto.getIdKhachHang())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));

        Staff nhanVien = nhanVienRepository.findById(dto.getIdNhanVien())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));

        Room phongChieu = phongChieuRepository.findById(dto.getIdPhongChieu())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng chiếu"));

        Invoice hoaDon = hoaDonRepository.findById(dto.getIdHoaDon())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn"));

        // 2. Kiểm tra ghế đã được đặt trong suất chiếu chưa
        if (vePhimRepo.existsByGheNgoiAndSuatChieu(gheNgoi, suatChieu)) {
            throw new BadRequestException("Ghế này đã được đặt trong suất chiếu này");
        }

        // 3. Parse enum trạng thái
        TrangThaiVePhim trangThai;
        try {
            trangThai = TrangThaiVePhim.valueOf(dto.getTrangThai().toUpperCase()); // để phòng client gửi lowercase
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Trạng thái vé không hợp lệ");
        }

        // 4. Gọi native insert
        vePhimRepo.insertVePhimNative(
                gheNgoi.getIdGheNgoi(),
                dto.getGiaVe(),
                hoaDon.getIdHoaDon(),
                khachHang.getIdKhachHang(),
                java.sql.Date.valueOf(dto.getNgayDat()),
                nhanVien.getIdNhanVien(),
                phim.getIdPhim(),
                suatChieu.getIdSuatChieu(),
                phongChieu.getIdPhongChieu(),
                trangThai.name()
        );

        // 5. Trả lại entity (chưa có ID vì insert native, có thể mapping lại nếu cần)
        CinemaTicket vePhim = new CinemaTicket();
        vePhim.setGiaVe(dto.getGiaVe());
        vePhim.setNgayDat(dto.getNgayDat());
        vePhim.setGheNgoi(gheNgoi);
        vePhim.setPhim(phim);
        vePhim.setSuatChieu(suatChieu);
        vePhim.setNhanVien(nhanVien);
        vePhim.setHoaDon(hoaDon);
        vePhim.setKhachHang(khachHang);
        vePhim.setPhongChieu(phongChieu);
        vePhim.setTrangThai(trangThai);

        // Convert Entity ➜ Response DTO
        return modelMapper.map(vePhim, CinemaTicketResponseDto.class);
    }

    @Override
    public CinemaTicketResponseDto getVePhimDtoById(Integer id) {
        CinemaTicket vePhim = vePhimRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy vé phim với id: " + id));
        return modelMapper.map(vePhim, CinemaTicketResponseDto.class);
    }

}
