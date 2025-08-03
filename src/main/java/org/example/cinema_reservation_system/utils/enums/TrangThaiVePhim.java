package org.example.cinema_reservation_system.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TrangThaiVePhim {
    CON_TRONG, DA_BAN, DA_DAT;

    @JsonCreator
    public static TrangThaiVePhim fromString(String value) {
        return TrangThaiVePhim.valueOf(value.toLowerCase());
    }
}
