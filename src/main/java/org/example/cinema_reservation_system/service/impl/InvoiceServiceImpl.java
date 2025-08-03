package org.example.cinema_reservation_system.service.impl;

import org.example.cinema_reservation_system.dto.invoicedto.InvoiceDto;
import org.example.cinema_reservation_system.entity.Invoice;
import org.example.cinema_reservation_system.entity.Customer;
import org.example.cinema_reservation_system.entity.Staff;
import org.example.cinema_reservation_system.entity.Voucher;
import org.example.cinema_reservation_system.repository.invoice.InvoiceRepository;
import org.example.cinema_reservation_system.repository.customer.CustomerRepository;
import org.example.cinema_reservation_system.repository.staff.StaffRepository;
import org.example.cinema_reservation_system.repository.voucher.VoucherRepository;
import org.example.cinema_reservation_system.service.InvoiceService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository hoaDonRepo;
    private final ModelMapper modelMapper;
    private final CustomerRepository khachHangRepo;
    private final StaffRepository nhanVienRepo;
    private final VoucherRepository voucherRepo;

    public InvoiceServiceImpl(InvoiceRepository hoaDonRepo, ModelMapper modelMapper,
                              CustomerRepository khachHangRepo,
                              StaffRepository nhanVienRepo,
                              VoucherRepository voucherRepo) {
        this.hoaDonRepo = hoaDonRepo;
        this.modelMapper = modelMapper;
        this.khachHangRepo = khachHangRepo;
        this.nhanVienRepo = nhanVienRepo;
        this.voucherRepo = voucherRepo;
        // Tạo TypeMap ánh xạ từ HoaDon -> HoaDonDto
        TypeMap<Invoice, InvoiceDto> typeMap = modelMapper.createTypeMap(Invoice.class, InvoiceDto.class);
        typeMap.addMappings(mapper -> {
            mapper.map(src -> src.getKhachHang().getTenKhachHang(), InvoiceDto::setTenKhachHang);
            mapper.map(src -> src.getKhachHang().getSoDienThoaiKhachHang(), InvoiceDto::setSoDienThoaiKhachHang);
            mapper.map(src -> src.getNhanVien().getTenNhanVien(), InvoiceDto::setTenNhanVien);
            mapper.map(src -> src.getVoucher().getTenVoucher(), InvoiceDto::setTenVoucher);
            mapper.map(src -> src.getVoucher().getSoTienGiam(), InvoiceDto::setSoTienGiam);
        });
    }

    @Override
    public List<InvoiceDto> getAllHoaDon() {
        return hoaDonRepo.findAll().stream()
                .map(hoaDon -> modelMapper.map(hoaDon, InvoiceDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Invoice createHoaDon(Invoice hoaDon) {
        // Bắt buộc phải đảm bảo các FK là object đầy đủ (not chỉ có ID)

        // Lấy và set thông tin khách hàng
        Customer kh = khachHangRepo.findById(hoaDon.getKhachHang().getIdKhachHang())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        hoaDon.setKhachHang(kh);
        hoaDon.setTenKhachHang(kh.getTenKhachHang());
        hoaDon.setSoDienThoai(kh.getSoDienThoaiKhachHang());

        // Nhân viên
        Staff nv = nhanVienRepo.findById(hoaDon.getNhanVien().getIdNhanVien())
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
        Invoice saved = hoaDonRepo.save(hoaDon);
        return modelMapper.map(saved, Invoice.class);
    }


}
