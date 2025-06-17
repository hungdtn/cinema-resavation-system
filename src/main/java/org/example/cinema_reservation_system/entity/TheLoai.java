package org.example.cinema_reservation_system.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "the_loai")
@Builder
public class TheLoai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTheLoai;

    @Column(name = "ten_the_loai", nullable = false, unique = true, length = 100)
    private String tenTheLoai;

    @ManyToMany(mappedBy = "theLoaiList")
    private List<Phim> phimList;

    public TheLoai(Integer id, String tenTheLoai) {
        this.idTheLoai = id;
        this.tenTheLoai = tenTheLoai;
    }

}
