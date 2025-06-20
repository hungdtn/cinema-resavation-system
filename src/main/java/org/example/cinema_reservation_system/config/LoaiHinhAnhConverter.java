package org.example.cinema_reservation_system.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.cinema_reservation_system.utils.LoaiHinhAnh;


@Converter(autoApply = true)
public class LoaiHinhAnhConverter implements AttributeConverter<LoaiHinhAnh, String> { //xử lý được cả "Poster" lẫn "POSTER"

    @Override
    public String convertToDatabaseColumn(LoaiHinhAnh attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public LoaiHinhAnh convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return LoaiHinhAnh.valueOf(dbData.toUpperCase()); // chuyển thành POSTER, BANNER
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid LoaiHinhAnh value from DB: " + dbData);
        }
    }
}
