package org.example.cinema_reservation_system.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phim_theloai")
public class PhimTheLoai {
    @Id
    @ManyToOne
    @JoinColumn(name = "id_phim")
    private Phim phim;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_the_loai_phim")
    private TheLoai theLoai;
}
