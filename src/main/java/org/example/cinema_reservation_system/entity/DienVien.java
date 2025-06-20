package org.example.cinema_reservation_system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dienvien")
public class DienVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dien_vien")
    private Integer idDienVien;

    @Column(name = "ten_dien_vien", nullable = false, length = 100)
    private String tenDienVien;


}
