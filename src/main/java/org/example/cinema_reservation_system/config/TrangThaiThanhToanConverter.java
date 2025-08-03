package org.example.cinema_reservation_system.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.cinema_reservation_system.utils.enums.TrangThaiThanhToan;


@Converter(autoApply = true)
public class TrangThaiThanhToanConverter implements AttributeConverter<TrangThaiThanhToan, String> {

    @Override
    public String convertToDatabaseColumn(TrangThaiThanhToan attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public TrangThaiThanhToan convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return TrangThaiThanhToan.valueOf(dbData.toUpperCase()); // đảm bảo mapping enum
    }
}