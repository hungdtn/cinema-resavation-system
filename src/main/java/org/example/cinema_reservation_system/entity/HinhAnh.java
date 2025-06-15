package org.example.cinema_reservation_system.entity;

import lombok.*;
import jakarta.persistence.*;
import org.example.cinema_reservation_system.config.LoaiHinhAnhConverter;
import org.example.cinema_reservation_system.utils.LoaiHinhAnh;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hinh_anh")
public class HinhAnh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHinhAnh;

    @Column(name = "ten_hinh_anh", nullable = false, length = 100)
    private String tenHinhAnh;

    @Column(name = "loai_hinh_anh", nullable = false)
    @Convert(converter = LoaiHinhAnhConverter.class)
    private LoaiHinhAnh loaiHinhAnh;


    @Column(name = "url", nullable = false)
    private String url;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_phim", nullable = false)
    @EqualsAndHashCode.Exclude
    private Phim phim;

}
