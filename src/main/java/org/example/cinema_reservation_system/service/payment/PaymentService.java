package org.example.cinema_reservation_system.service.payment;

import org.example.cinema_reservation_system.dto.payment.PaymentRequestDto;
import org.example.cinema_reservation_system.dto.payment.PaymentResponseDto;

public interface PaymentService {
    PaymentResponseDto thanhToanHoaDon(PaymentRequestDto request);
    String createVNPayPayment(PaymentRequestDto request);
    String createMoMoPayment(PaymentRequestDto request);
}
