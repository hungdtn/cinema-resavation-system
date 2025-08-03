package org.example.cinema_reservation_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trailer")
public class Trailer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTrailer;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;

    @OneToOne
    @JoinColumn(name = "id_phim", nullable = false)
    private Movie phim;
}
