package org.example.cinema_reservation_system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phim_dienvien")
public class PhimDienVien {
    @Id
    @ManyToOne
    @JoinColumn(name = "id_phim")
    private Phim phim;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_dien_vien")
    private DienVien dienVien;
}
