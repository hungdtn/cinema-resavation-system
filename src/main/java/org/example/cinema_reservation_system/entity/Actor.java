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
@Table(name = "dien_vien")
@Builder
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDienVien;

    @Column(name = "ten_dien_vien", nullable = false, length = 100)
    private String tenDienVien;

    @ManyToMany(mappedBy = "dienVienList")
    private List<Movie> phimList;

    public Actor(Integer id, String tenDienVien) {
        this.idDienVien = id;
        this.tenDienVien = tenDienVien;
    }

}
