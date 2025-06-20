package org.example.cinema_reservation_system.entity;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.ToString;
import org.example.cinema_reservation_system.utils.Enum;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phim")
public class Phim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phim")
    private Integer idPhim;

    @Column(name = "ten_phim", nullable = false, unique = true, length = 100)
    private String tenPhim;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "thoi_luong", nullable = false)
    private Integer thoiLuong;

    @Column(name = "ngay_phat_hanh", nullable = false)
    private LocalDate ngayPhatHanh;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, columnDefinition = "enum_trang_thai_phim")
    private Enum.TrangThaiPhim trangThai;

    @Column(name = "dinh_dang", nullable = false, length = 50)
    @Pattern(regexp = "2D|3D|4D|IMAX", message = "Định dạng phải là 2D, 3D, 4D hoặc IMAX")
    private String dinhDang;

    @Column(name = "ngay_tao", nullable = false)
    private LocalDate ngayTao;

    @PrePersist
    protected void onCreate() {
        this.ngayTao = LocalDate.now();
    }

    @ToString.Exclude
    @OneToMany(mappedBy = "phim", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<HinhAnh> hinhAnhs = new HashSet<>();

    @OneToOne(mappedBy = "phim", cascade = CascadeType.ALL, orphanRemoval = true)
    private Trailer trailer;

    // ====================== QUAN HỆ ==========================

    @ManyToMany
    @JoinTable(
            name = "phim_the_loai",
            joinColumns = @JoinColumn(name = "id_phim"),
            inverseJoinColumns = @JoinColumn(name = "id_the_loai_phim")
    )
    private Set<TheLoai> theLoaiList;

    @ManyToMany
    @JoinTable(
            name = "phim_dao_dien",
            joinColumns = @JoinColumn(name = "id_phim"),
            inverseJoinColumns = @JoinColumn(name = "id_dao_dien")
    )
    private Set<DaoDien> daoDienList;

    @ManyToMany
    @JoinTable(
            name = "phim_dien_vien",
            joinColumns = @JoinColumn(name = "id_phim"),
            inverseJoinColumns = @JoinColumn(name = "id_dien_vien")
    )
    private Set<DienVien> dienVienList;

}
