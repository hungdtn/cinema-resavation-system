package org.example.cinema_reservation_system.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

/**
 * Exception dùng cho tất cả các lỗi nghiệp vụ (business logic).
 * Thrown ra khi muốn trả về một HTTP error code kèm message, errorCode riêng
 * và có thể thêm chi tiết (details).
 */
@Getter
public class BusinessException extends RuntimeException {
    /**
     * Mã lỗi (do bạn định nghĩa, ví dụ: SEAT_NOT_FOUND, DUPLICATE_SEAT,…).
     */
    private final String errorCode;

    /**
     * HTTP status sẽ trả về. Mặc định BAD_REQUEST nếu không truyền.
     */
    private final HttpStatus status;

    /**
     * Thời điểm exception được sinh ra.
     */
    private final LocalDateTime timestamp;

    /**
     * Map chứa thêm các trường chi tiết (nếu cần).
     */
    private final Map<String, Object> details;

    //–––––––––––––––––––––––––––––––––––––––––––––––––––
    // Các constructor tiện lợi
    //–––––––––––––––––––––––––––––––––––––––––––––––––––

    /**
     * Mặc định: BAD_REQUEST, errorCode = "BUSINESS_ERROR", no details.
     */
    public BusinessException(String message) {
        this(message, "BUSINESS_ERROR", HttpStatus.BAD_REQUEST, Collections.emptyMap());
    }

    /**
     * Mặc định BAD_REQUEST, với custom code.
     */
    public BusinessException(String message, String errorCode) {
        this(message, errorCode, HttpStatus.BAD_REQUEST, Collections.emptyMap());
    }

    /**
     * Mặc định code = "BUSINESS_ERROR", custom status.
     */
    public BusinessException(String message, HttpStatus status) {
        this(message, "BUSINESS_ERROR", status, Collections.emptyMap());
    }

    /**
     * Custom code + custom status.
     */
    public BusinessException(String message, String errorCode, HttpStatus status) {
        this(message, errorCode, status, Collections.emptyMap());
    }

    /**
     * Custom full: message, code, status và thêm details map.
     */
    public BusinessException(String message,
                             String errorCode,
                             HttpStatus status,
                             Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.details = details == null ? Collections.emptyMap() : details;
    }
}
