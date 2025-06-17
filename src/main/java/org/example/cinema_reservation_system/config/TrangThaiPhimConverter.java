package org.example.cinema_reservation_system.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.cinema_reservation_system.utils.Enum;

@Converter(autoApply = true)
public class TrangThaiPhimConverter implements AttributeConverter<Enum.TrangThaiPhim, String> {
    @Override
    public String convertToDatabaseColumn(Enum.TrangThaiPhim attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public Enum.TrangThaiPhim convertToEntityAttribute(String dbData) {
        try {
            return dbData != null ? Enum.TrangThaiPhim.valueOf(dbData.toUpperCase()) : null;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid value for TrangThaiPhim enum: " + dbData);
        }
    }
}
