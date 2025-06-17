package org.example.cinema_reservation_system.service.impl;

import org.example.cinema_reservation_system.dto.HoaDonDto;
import org.example.cinema_reservation_system.entity.HoaDon;
import org.example.cinema_reservation_system.entity.KhachHang;
import org.example.cinema_reservation_system.entity.NhanVien;
import org.example.cinema_reservation_system.entity.Voucher;
import org.example.cinema_reservation_system.repository.HoaDonRepo;
import org.example.cinema_reservation_system.repository.KhachHangRepo;
import org.example.cinema_reservation_system.repository.NhanVienRepo;
import org.example.cinema_reservation_system.repository.VoucherRepo;
import org.example.cinema_reservation_system.service.HoaDonService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HoaDonServiceImpl implements HoaDonService {

    private final HoaDonRepo hoaDonRepo;
    private final ModelMapper modelMapper;
    private final KhachHangRepo khachHangRepo;
    private final NhanVienRepo nhanVienRepo;
    private final VoucherRepo voucherRepo;

    public HoaDonServiceImpl(HoaDonRepo hoaDonRepo, ModelMapper modelMapper,
                             KhachHangRepo khachHangRepo,
                             NhanVienRepo nhanVienRepo,
                             VoucherRepo voucherRepo) {
        this.hoaDonRepo = hoaDonRepo;
        this.modelMapper = modelMapper;
        this.khachHangRepo = khachHangRepo;
        this.nhanVienRepo = nhanVienRepo;
        this.voucherRepo = voucherRepo;
        // Tạo TypeMap ánh xạ từ HoaDon -> HoaDonDto
        TypeMap<HoaDon, HoaDonDto> typeMap = modelMapper.createTypeMap(HoaDon.class, HoaDonDto.class);
        typeMap.addMappings(mapper -> {
            mapper.map(src -> src.getKhachHang().getTenKhachHang(), HoaDonDto::setTenKhachHang);
            mapper.map(src -> src.getKhachHang().getSoDienThoaiKhachHang(), HoaDonDto::setSoDienThoaiKhachHang);
            mapper.map(src -> src.getNhanVien().getTenNhanVien(), HoaDonDto::setTenNhanVien);
            mapper.map(src -> src.getVoucher().getTenVoucher(), HoaDonDto::setTenVoucher);
            mapper.map(src -> src.getVoucher().getSoTienGiam(), HoaDonDto::setSoTienGiam);
        });
    }

    @Override
    public List<HoaDonDto> getAllHoaDon() {
        return hoaDonRepo.findAll().stream()
                .map(hoaDon -> modelMapper.map(hoaDon, HoaDonDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public HoaDon createHoaDon(HoaDon hoaDon) {
        // Bắt buộc phải đảm bảo các FK là object đầy đủ (not chỉ có ID)

        // Lấy và set thông tin khách hàng
        KhachHang kh = khachHangRepo.findById(hoaDon.getKhachHang().getIdKhachHang())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        hoaDon.setKhachHang(kh);
        hoaDon.setTenKhachHang(kh.getTenKhachHang());
        hoaDon.setSoDienThoai(kh.getSoDienThoaiKhachHang());

        // Nhân viên
        NhanVien nv = nhanVienRepo.findById(hoaDon.getNhanVien().getIdNhanVien())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        hoaDon.setNhanVien(nv);

        // Voucher (có thể null)
        if (hoaDon.getVoucher() != null && hoaDon.getVoucher().getIdVoucher() != null) {
            Voucher voucher = voucherRepo.findById(hoaDon.getVoucher().getIdVoucher())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy voucher"));
            hoaDon.setVoucher(voucher);
        } else {
            hoaDon.setVoucher(null);
        }

        // Lưu và trả DTO
        HoaDon saved = hoaDonRepo.save(hoaDon);
        return modelMapper.map(saved, HoaDon.class);
    }


}
