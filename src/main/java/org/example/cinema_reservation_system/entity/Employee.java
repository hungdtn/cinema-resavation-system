package org.example.cinema_reservation_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nhan_vien")
public class Employee {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_nhan_vien")
        private Integer id;

        @Column(name = "ten_nhan_vien")
        private String tenNhanVien;

        @Column(name = "email")
        private String email;

        @Column(name = "so_dien_thoai")
        private String soDienThoai;

}
