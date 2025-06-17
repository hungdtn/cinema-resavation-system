package org.example.cinema_reservation_system.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dao_dien")
@Builder
public class DaoDien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDaoDien;

    @Column(name = "ten_dao_dien", nullable = false, length = 100)
    private String tenDaoDien;

    @ManyToMany(mappedBy = "daoDienList")
    private List<Phim> phimList;

    public DaoDien(Integer id, String tenDaoDien) {
        this.idDaoDien = id;
        this.tenDaoDien = tenDaoDien;
    }
}
