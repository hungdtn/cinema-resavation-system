package org.example.cinema_reservation_system.service.payment;

import java.math.BigDecimal;

public interface VnPayService {
    String createPaymentUrl(BigDecimal amount, String orderInfo);
    String hmacSHA512(String key, String data);
    String getSecret();
}
