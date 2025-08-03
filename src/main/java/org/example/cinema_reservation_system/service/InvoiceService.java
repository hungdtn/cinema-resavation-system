package org.example.cinema_reservation_system.service;

import org.example.cinema_reservation_system.dto.invoicedto.InvoiceDto;
import org.example.cinema_reservation_system.entity.Invoice;

import java.util.List;

public interface InvoiceService {
    List<InvoiceDto> getAllHoaDon();
    Invoice createHoaDon(Invoice hoaDon);
}
