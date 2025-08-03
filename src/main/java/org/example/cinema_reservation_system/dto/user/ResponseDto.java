package org.example.cinema_reservation_system.dto.user;

import org.example.cinema_reservation_system.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder //ko cần new Object
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    private String code;
    private String message;
    public T data;

    public static <T> ResponseDto<T> success(T data) {
        return ResponseDto.<T>builder()
                .code(Constants.CODE_SUCCESS)
                .message(Constants.MESSAGE_SUCCESS)
                .data(data)
                .build();
    }

    public static <T> ResponseDto<T> fail(String code, String message) {
        return ResponseDto.<T>builder()
                .code(code)
                .message(message)
                .build();
    }

}
