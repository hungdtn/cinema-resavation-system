package org.example.cinema_reservation_system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DaoDien")
public class DaoDien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDaoDien;

    @Column(name = "ten_dao_dien", nullable = false, length = 100)
    private String tenDaoDien;
}
