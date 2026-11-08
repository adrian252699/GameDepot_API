package com.example.GameDepotAPI.repository;

import com.example.GameDepotAPI.model.carrito.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    Optional<ItemCarrito> findByCarritoIdAndVideojuegoId(
            Long carritoId,
            Long productoId
    );

    Optional<ItemCarrito> findByIdAndCarritoUsuarioId(
            Long itemId,
            Long usuarioId
    );
}
