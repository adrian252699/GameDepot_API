package com.example.GameDepotAPI.repository;

import com.example.GameDepotAPI.dto.enums.EstadoOrden;
import com.example.GameDepotAPI.dto.enums.EstadoPago;
import com.example.GameDepotAPI.model.orden.Orden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    @EntityGraph(attributePaths = {"items", "items.videojuego"})
    Page<Orden> findByUsuarioId(Long usuarioId, Pageable pageable);

    @EntityGraph(attributePaths = {"items", "items.videojuego"})
    Page<Orden> findByUsuarioIdAndEstado(Long usuarioId, EstadoOrden estado, Pageable pageable);

    @EntityGraph(attributePaths = {"items", "items.videojuego"})
    Page<Orden> findByEstado(EstadoOrden estado, Pageable pageable);

    @EntityGraph(attributePaths = {"items", "items.videojuego"})
    Optional<Orden> findConItemsById(Long id);

    Optional<Orden> findByIdAndUsuarioId(Long ordenId, Long usuarioId);

    boolean existsByFolio(String folio);

    long countByEstado(EstadoOrden estado);

    //Suma las ventas menos de las que tienen estado cancelado
    @Query("""

            SELECT COALESCE(SUM(o.total), 0)
       FROM Orden o
       WHERE o.estado <> :estadoCancelado
       """)
    BigDecimal sumarVentasExceptoEstado(@Param("estadoCancelado") EstadoOrden estadoCancelado);

    Optional<Orden> findFirstByUsuario_IdAndEstadoPagoOrderByFechaDesc(
            Long usuarioId,
            EstadoPago estadoPago
    );

    @Query("""
        select distinct o
        from Orden o
        left join fetch o.items i
        left join fetch i.videojuego v
        left join fetch v.categoria
        where o.id = :ordenId
          and o.usuario.id = :usuarioId
        """)
    Optional<Orden> findDetalleByIdAndUsuarioId(@Param("ordenId") Long ordenId, @Param("usuarioId") Long usuarioId);

    @Query("""
        select distinct o
        from Orden o
        left join fetch o.usuario u
        left join fetch o.items i
        left join fetch i.videojuego v
        left join fetch v.categoria c
        where o.id = :ordenId
        """)
    Optional<Orden> findDetalleAdminById(@Param("ordenId") Long ordenId);
}
