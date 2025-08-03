package org.example.cinema_reservation_system.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerStatDTO {
    private Integer customerId;
    private String customerName;
    private Integer totalTickets;
}
