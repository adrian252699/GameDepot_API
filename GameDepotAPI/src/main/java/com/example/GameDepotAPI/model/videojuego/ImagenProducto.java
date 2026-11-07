package com.example.GameDepotAPI.model.videojuego;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImagenProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "videojuego_id")
    private Videojuego videojuego;
}
