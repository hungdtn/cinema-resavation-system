package org.example.cinema_reservation_system.controller.payment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.payment.PaymentRequestDto;
import org.example.cinema_reservation_system.dto.payment.PaymentResponseDto;
import org.example.cinema_reservation_system.service.payment.PaymentService;
import org.example.cinema_reservation_system.service.payment.VnPayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService thanhToanService;

    private final VnPayService vnPayService;


    @PostMapping
    public ResponseEntity<PaymentResponseDto> thanhToan(@Valid @RequestBody PaymentRequestDto request) {
        return ResponseEntity.ok(thanhToanService.thanhToanHoaDon(request));
    }

    @PostMapping("/create-vnpay")
    public ResponseEntity<String> createVnPayPayment(@RequestParam BigDecimal amount,
                                                     @RequestParam String orderInfo) {
        String paymentUrl = vnPayService.createPaymentUrl(amount, orderInfo);
        return ResponseEntity.ok(paymentUrl);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<String> vnPayReturn(@RequestParam Map<String, String> params) {
        String vnp_SecureHash = params.remove("vnp_SecureHash");

        // Bước 1: Sắp xếp lại các key A-Z
        List<String> sortedKeys = new ArrayList<>(params.keySet());
        Collections.sort(sortedKeys);

        // Bước 2: Tạo chuỗi HashData
        StringBuilder hashData = new StringBuilder();
        for (String key : sortedKeys) {
            String value = params.get(key);
            if (value != null && !value.isEmpty()) {
                hashData.append(key).append('=').append(value).append('&');
            }
        }
        // Xoá dấu & cuối
        hashData.setLength(hashData.length() - 1);

        // Bước 3: Tính lại SecureHash
        String calculatedHash = vnPayService.hmacSHA512(vnPayService.getSecret(), hashData.toString());

        // Bước 4: So sánh
        if (!calculatedHash.equalsIgnoreCase(vnp_SecureHash)) {
            return ResponseEntity.badRequest().body("Sai chữ ký! Giao dịch không hợp lệ.");
        }

        // Bước 5: Xử lý kết quả từ VNPay
        String responseCode = params.get("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            return ResponseEntity.ok("💰 Thanh toán thành công!");
        } else {
            return ResponseEntity.ok("❌ Thanh toán thất bại. Mã lỗi: " + responseCode);
        }
    }


    @PostMapping("/momo")
    public ResponseEntity<PaymentResponseDto> createMoMoPayment(@Valid @RequestBody PaymentRequestDto request) {
        if (request.getScheduleId() == null || request.getSeatIds() == null || request.getSeatIds().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new PaymentResponseDto(null, "Thiếu scheduleId hoặc seatIds")
            );
        }

        String paymentUrl = thanhToanService.createMoMoPayment(request);
        System.out.println("✅ MoMo paymentUrl: " + paymentUrl);

        if (paymentUrl == null) {
            return ResponseEntity.internalServerError().body(
                    new PaymentResponseDto(null, "Không tạo được link thanh toán MoMo")
            );
        }

        return ResponseEntity.ok(new PaymentResponseDto(paymentUrl));
    }
}
