package org.example.cinema_reservation_system.dto.moviedto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrailerRequestDto {
    @NotBlank(message = "Link trailer không được để trống")
    @Pattern(regexp = "^(https?\\:\\/\\/)?(www\\.youtube\\.com|youtu\\.be)\\/.+$",
            message = "Link trailer phải là URL YouTube hợp lệ")
    private String url;

    @NotNull(message = "ID phim không được để trống")
    private Integer idPhim;
}
