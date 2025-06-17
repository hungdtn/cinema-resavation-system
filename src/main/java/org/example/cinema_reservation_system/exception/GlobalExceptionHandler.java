package org.example.cinema_reservation_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

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
        ex.printStackTrace();

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
        Throwable cause = ex.getCause();

        if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException invalidEx) {
            String field = invalidEx.getPath().isEmpty() ? "Không xác định" : invalidEx.getPath().get(0).getFieldName();
            Class<?> targetType = invalidEx.getTargetType();

            if (targetType.isEnum()) {
                Object[] enumConstants = targetType.getEnumConstants();
                String allowedValues = Arrays.stream(enumConstants)
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
                error.put("error", "Trường '" + field + "' có giá trị không hợp lệ. Giá trị hợp lệ: " + allowedValues + ".");
            } else {
                error.put("error", "Trường '" + field + "' có kiểu dữ liệu không hợp lệ.");
            }
        } else {
            error.put("error", "Dữ liệu JSON không hợp lệ. Vui lòng kiểm tra định dạng.");
        }

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }




    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
