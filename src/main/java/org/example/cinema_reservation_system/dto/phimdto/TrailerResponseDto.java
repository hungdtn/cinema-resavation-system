package org.example.cinema_reservation_system.dto.phimdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrailerResponseDto {
    private Integer idTrailer;
    private String url;
    private Integer idPhim;
    private String tenPhim;
}
