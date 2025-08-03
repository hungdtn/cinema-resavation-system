package org.example.cinema_reservation_system.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.cinema_reservation_system.utils.enums.TrangThaiPhim;

@Converter(autoApply = true)
public class TrangThaiPhimConverter implements AttributeConverter<TrangThaiPhim, String> {
    @Override
    public String convertToDatabaseColumn(TrangThaiPhim attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override

    public TrangThaiPhim convertToEntityAttribute(String dbData) {
        try {
            return dbData != null ? TrangThaiPhim.valueOf(dbData.toUpperCase()) : null;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid value for TrangThaiPhim enum: " + dbData);
        }
    }
}
