package org.example.cinema_reservation_system.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TrangThaiVePhim {
    con_trong,
    da_ban,
    da_dat;

    @JsonCreator
    public static TrangThaiVePhim fromString(String value) {
        return TrangThaiVePhim.valueOf(value.toLowerCase());
    }
}
