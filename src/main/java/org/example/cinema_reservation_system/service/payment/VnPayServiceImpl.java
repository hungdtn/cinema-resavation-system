package org.example.cinema_reservation_system.service.payment;

import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.config.VnPayConfig;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VnPayServiceImpl implements VnPayService {

    private final VnPayConfig vnPayConfig;

    @Override
    public String createPaymentUrl(BigDecimal amount, String orderInfo) {
        try {
            // 1. Khởi tạo tham số cơ bản
            String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
            String vnp_IpAddr = "127.0.0.1";
            String vnp_TmnCode = vnPayConfig.getTmnCode();
            String vnp_HashSecret = vnPayConfig.getHashSecret();
            String vnp_ReturnUrl = vnPayConfig.getReturnUrl();
            String vnp_Url = vnPayConfig.getPayUrl();

            // 2. Gán tham số vào map
            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put("vnp_Version", "2.1.0");
            vnpParams.put("vnp_Command", "pay");
            vnpParams.put("vnp_TmnCode", vnp_TmnCode);
            vnpParams.put("vnp_Amount", String.valueOf(amount.multiply(BigDecimal.valueOf(100)).longValue()));
            vnpParams.put("vnp_CurrCode", "VND");
            vnpParams.put("vnp_TxnRef", vnp_TxnRef);
            vnpParams.put("vnp_OrderInfo", orderInfo.trim());
            vnpParams.put("vnp_OrderType", "other"); // hoặc "billpayment"
            vnpParams.put("vnp_Locale", "vn");
            vnpParams.put("vnp_ReturnUrl", vnp_ReturnUrl); // KHÔNG ENCODE trước!
            vnpParams.put("vnp_IpAddr", vnp_IpAddr);
            vnpParams.put("vnp_CreateDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

            // 3. Sắp xếp và tạo chuỗi hash + query
            List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
            Collections.sort(fieldNames); // sort theo tên gốc

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            for (String key : fieldNames) {
                String value = vnpParams.get(key);
                if (value != null && !value.isEmpty()) {
                    hashData.append(key).append('=').append(value).append('&'); // ❌ KHÔNG encode ở đây
                    query.append(URLEncoder.encode(key, StandardCharsets.UTF_8.toString()))
                            .append('=')
                            .append(URLEncoder.encode(value, StandardCharsets.UTF_8.toString()))
                            .append('&');
                }
            }
            hashData.setLength(hashData.length() - 1);
            query.setLength(query.length() - 1);

            String secureHash = hmacSHA512(vnp_HashSecret, hashData.toString());

            // 5. In log kiểm tra
            System.out.println("✅ hashData = " + hashData);
            System.out.println("✅ secureHash = " + secureHash);

            // 6. Trả về URL hoàn chỉnh
            return vnp_Url + "?" + query + "&vnp_SecureHash=" + secureHash;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo URL thanh toán VNPay", e);
        }
    }

    @Override
    public String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b)); // chữ thường ✅
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo chữ ký HMAC SHA512", e);
        }
    }

    @Override
    public String getSecret() {
        return vnPayConfig.getHashSecret();
    }
}
