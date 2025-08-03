package org.example.cinema_reservation_system.service.payment;

import jakarta.transaction.Transactional;
import org.example.cinema_reservation_system.dto.payment.PaymentRequestDto;
import org.example.cinema_reservation_system.dto.payment.PaymentResponseDto;
import org.example.cinema_reservation_system.entity.*;
import org.example.cinema_reservation_system.exception.ResourceNotFoundException;
import org.example.cinema_reservation_system.repository.invoice.InvoiceRepository;
import org.example.cinema_reservation_system.repository.payment.PaymentRepository;
import org.example.cinema_reservation_system.repository.cinematicket.CinemaTicketRepository;
import org.example.cinema_reservation_system.repository.seat.SeatRepository;
import org.example.cinema_reservation_system.repository.showtime.ShowTimeRepository;
import org.example.cinema_reservation_system.utils.Enum;
import org.example.cinema_reservation_system.utils.enums.TrangThaiHoaDon;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final InvoiceRepository hoaDonRepository;
    private final PaymentRepository thanhToanRepository;
    private final CinemaTicketRepository vePhimRepository;
    private final ShowTimeRepository suatChieuRepository;
    private final SeatRepository gheNgoiRepository;

    public PaymentServiceImpl(InvoiceRepository hoaDonRepository,
                              PaymentRepository thanhToanRepository,
                              CinemaTicketRepository vePhimRepository, ShowTimeRepository suatChieuRepository, SeatRepository gheNgoiRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.thanhToanRepository = thanhToanRepository;
        this.vePhimRepository = vePhimRepository;
        this.suatChieuRepository = suatChieuRepository;
        this.gheNgoiRepository = gheNgoiRepository;
    }

    @Override
    @Transactional
    public PaymentResponseDto thanhToanHoaDon(PaymentRequestDto request) {
        Invoice hoaDon = hoaDonRepository.findById(request.getIdHoaDon())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn"));

        List<CinemaTicket> veList = vePhimRepository.findAllByHoaDon_IdHoaDon(hoaDon.getIdHoaDon());
        if (veList.isEmpty()) throw new RuntimeException("Hóa đơn chưa có vé nào");

        BigDecimal tongTien = veList.stream()
                .map(CinemaTicket::getGiaVe)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Payment thanhToan = new Payment();
        thanhToan.setHoaDon(hoaDon);
        thanhToan.setPhuongThucThanhToan(request.getPhuongThucThanhToan());
        thanhToan.setSoTien(tongTien);
        thanhToan.setNgayThanhToan(LocalDate.now());
        thanhToan.setTrangThai(org.example.cinema_reservation_system.utils.Enum.TrangThaiThanhToan.HOAN_THANH);

        thanhToanRepository.save(thanhToan);

        PaymentResponseDto response = new PaymentResponseDto();
        response.setIdThanhToan(thanhToan.getIdThanhToan());
        response.setSoTien(tongTien);
        response.setPhuongThucThanhToan(thanhToan.getPhuongThucThanhToan());
        response.setTrangThai(thanhToan.getTrangThai());
        response.setNgayThanhToan(thanhToan.getNgayThanhToan());

        return response;
    }

    @Override
    public String createVNPayPayment(PaymentRequestDto request) {
        // ✅ Validate dữ liệu đầu vào
        if (request.getScheduleId() == null || request.getSeatIds() == null || request.getSeatIds().isEmpty()) {
            throw new IllegalArgumentException("Thiếu scheduleId hoặc danh sách ghế");
        }

        // ✅ Lấy thông tin suất chiếu
        ShowTime suatChieu = suatChieuRepository.findById(Integer.parseInt(request.getScheduleId()))
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy suất chiếu"));

        // ✅ Tính tổng tiền dựa trên danh sách ghế
        BigDecimal tongTien = BigDecimal.ZERO;

        for (String seatId : request.getSeatIds()) {
            Seat ghe = gheNgoiRepository.findById(Integer.parseInt(seatId))
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ghế: " + seatId));

            BigDecimal giaVe = ghe.getLoaiGhe().equalsIgnoreCase("VIP")
                    ? new BigDecimal("90000")
                    : new BigDecimal("70000");

            tongTien = tongTien.add(giaVe);
        }

        // ✅ Giả lập tạo link thanh toán VNPay (sandbox)
        String fakeLink = "https://sandbox.vnpayment.vn/payment-link?" +
                "scheduleId=" + request.getScheduleId() +
                "&amount=" + tongTien +
                "&returnUrl=http://localhost:5173/payment-success";

        return fakeLink;
    }



    @Override
    public String createMoMoPayment(PaymentRequestDto request) {
        if (request.getScheduleId() == null || request.getSeatIds() == null || request.getSeatIds().isEmpty()) {
            System.out.println("❌ scheduleId hoặc seatIds null/empty");
            return null;
        }
        String fakeUrl = "https://test-payment.momo.vn/payment-link?scheduleId="
                + request.getScheduleId() + "&seats=" + String.join(",", request.getSeatIds());
        return fakeUrl;
    }
}
