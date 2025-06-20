package org.example.cinema_reservation_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler  {

    // Xử lý lỗi không tìm thấy
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Xử lý lỗi validate từ @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Xử lý lỗi khi @RequestParam hoặc @PathVariable sai kiểu (e.g. truyền "abc" cho Integer)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Sai kiểu dữ liệu cho tham số: " + ex.getName());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Xử lý lỗi chung còn lại
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralError(Exception ex) {
        ex.printStackTrace();  // In lỗi ra console để tiện debug

        Map<String, String> error = new HashMap<>();
        error.put("error", "Lỗi hệ thống: " + (ex.getMessage() != null ? ex.getMessage() : "Không xác định"));

        // Lấy dòng lỗi đầu tiên trong stack trace
        StackTraceElement[] stackTrace = ex.getStackTrace();
        if (stackTrace.length > 0) {
            StackTraceElement e = stackTrace[0];
            error.put("location", e.getClassName() + ":" + e.getLineNumber());
        }

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        String message = ex.getMessage();

        if (message != null && message.contains("TrangThaiRapChieu")) {
            error.put("error", "Trạng thái không hợp lệ. Vui lòng dùng: HOAT_DONG, KHONG_HOAT_DONG, BAO_TRI.");
        } else if (ex.getCause() instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
            error.put("error", "Giá trị truyền vào không hợp lệ. Kiểm tra định dạng các trường enum hoặc kiểu dữ liệu.");
        } else {
            error.put("error", "Dữ liệu không đọc được. Vui lòng kiểm tra định dạng JSON.");
        }
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<String> handleFileStorageException(FileStorageException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi lưu file: " + ex.getMessage());
    }

}
