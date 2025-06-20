package org.example.cinema_reservation_system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hinhanh")
public class HinhAnh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hinh_anh")
    private Integer idHinhAnh;

    @Column(name = "ten_hinh_anh", nullable = false, length = 100)
    private String tenHinhAnh;

    @Column(name = "loai_hinh_anh", nullable = false, length = 100)
    private String loaiHinhAnh;

    @ManyToOne
    @JoinColumn(name = "id_phim")
    private Phim phim;
}
