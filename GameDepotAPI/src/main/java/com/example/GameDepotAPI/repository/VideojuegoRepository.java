package com.example.GameDepotAPI.repository;

import com.example.GameDepotAPI.model.videojuego.Videojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideojuegoRepository extends JpaRepository<Videojuego, Long> {
    @Query("""
        SELECT v
        FROM Videojuego v
        WHERE v.id = :id
        """)
    Optional<Videojuego> findByIdForUpdate(@Param("id") Long id);
}
