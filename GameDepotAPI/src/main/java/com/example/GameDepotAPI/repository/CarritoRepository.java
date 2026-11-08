package com.example.GameDepotAPI.repository;

import com.example.GameDepotAPI.model.carrito.Carrito;
import com.example.GameDepotAPI.model.carrito.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuarioId(Long usuarioId);

    @Query("""
        SELECT DISTINCT c
        FROM Carrito c
        LEFT JOIN FETCH c.items i
        LEFT JOIN FETCH i.videojuego v
        LEFT JOIN FETCH v.categoria
        WHERE c.usuario.id = :usuarioId
    """)
    Optional<Carrito> findDetalleByUsuarioId(@Param("usuarioId") Long usuarioId);
}
