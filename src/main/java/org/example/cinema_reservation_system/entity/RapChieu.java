package org.example.cinema_reservation_system.entity;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.example.cinema_reservation_system.utils.Enum;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rap_chieu")
public class RapChieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rap_chieu")
    private Integer idRapChieu;

    @Column(name = "ten_rap_chieu", nullable = false, unique = true, length = 100)
    private String tenRapChieu;

    @Column(name = "dia_chi", nullable = false, length = 255)
    private String diaChi;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, columnDefinition = "enum_trang_thai_rap_chieu")
    private Enum.TrangThaiRapChieu trangThaiRapChieu;

    @Pattern(regexp = "^0[0-9]{9}$", message = "Số điện thoại không hợp lệ")
    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @OneToMany(mappedBy = "rapChieu", cascade = CascadeType.ALL)
    private List<PhongChieu> danhSachPhongChieu;

    @Override
    public String toString() {
        return "RapChieu{" +
                "idRapChieu=" + idRapChieu +
                ", tenRapChieu='" + tenRapChieu + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", trangThai=" + trangThaiRapChieu +
                ", soDienThoai='" + soDienThoai + '\'' +
                '}';
    }
}
